import dataclasses
import json
import logging
from typing import Any, Callable, Dict, Iterable, List, Optional, Sequence, Tuple
from urllib.parse import urlparse

from datahub.configuration.common import AllowDenyPattern
from datahub.emitter.mce_builder import make_dataset_urn_with_platform_instance
from datahub.emitter.mcp_builder import ContainerKey, gen_containers
from datahub.ingestion.api.common import PipelineContext
from datahub.ingestion.api.decorators import (
    SupportStatus,
    capability,
    config_class,
    platform_name,
    support_status,
)
from datahub.ingestion.api.source import (
    CapabilityReport,
    SourceCapability,
    TestableSource,
    TestConnectionReport,
)
from datahub.ingestion.api.workunit import MetadataWorkUnit
from datahub.ingestion.source.common.subtypes import (
    DataFlowSubTypes,
    DataJobSubTypes,
    GenericContainerSubTypes,
    SourceCapabilityModifier,
)
from datahub.ingestion.source.snowflake.snowflake_connection import SnowflakeConnection
from datahub.ingestion.source.snowflake.snowflake_openflow_config import (
    SnowflakeOpenflowSourceConfig,
)
from datahub.ingestion.source.snowflake.snowflake_openflow_models import (
    OpenflowConnector,
    OpenflowDeployment,
    OpenflowRuntime,
    merge_show_and_history,
)
from datahub.ingestion.source.snowflake.snowflake_openflow_query import (
    SnowflakeOpenflowQuery,
)
from datahub.ingestion.source.snowflake.snowflake_openflow_report import (
    SnowflakeOpenflowReport,
)
from datahub.ingestion.source.snowflake.snowflake_utils import (
    SnowflakeIdentifierBuilder,
)
from datahub.ingestion.source.state.stateful_ingestion_base import (
    StatefulIngestionSourceBase,
)
from datahub.sdk.dataflow import DataFlow
from datahub.sdk.datajob import DataJob

logger = logging.getLogger(__name__)

PLATFORM = "openflow"

SNOWFLAKE_SOURCE_HINT = (
    "Not applicable: Openflow moves data and holds no catalog of its own. Use the "
    "`snowflake` source for the destination tables."
)

# --- Config-derived lineage constants ---------------------------------------

CONFIG_FILENAME = "config.json"
SECTION_SOURCE = "Source"
SECTION_REPLICATION = "Replication table schema"
SECTION_DESTINATION = "Destination details"
# The observed property name is "Source Database Connection URL" (probe Result 37,
# section [0] "Source"), NOT "JDBC URL". Both are tried because different connector
# definitions may name it differently -- OPENFLOW_POSTGRES_CDC is the only one
# observed. A miss here is silent: source_database stays None, so no upstream inlet
# is ever built and lineage emits destinations only, with nothing reporting why.
PROP_SOURCE_URL_CANDIDATES = ("Source Database Connection URL", "JDBC URL")
PROP_INCLUDED_TABLE_NAMES = "Included Comma Separated Source Table Names"
PROP_INCLUDED_TABLE_PATTERN = "Included Source Table Pattern"
PROP_DESTINATION_DATABASE = "Snowflake Destination Database"
PROP_SCHEMA_STRATEGY = "Destination Schema Strategy"
SCHEMA_STRATEGY_SOURCE_SCHEMA = "SOURCE_SCHEMA"

# CONNECTOR_DEFINITION -> DataHub platform for the upstream side.
CONNECTOR_DEFINITION_PLATFORM = {
    "OPENFLOW_POSTGRES_CDC": "postgres",
    "OPENFLOW_MYSQL_CDC": "mysql",
    "OPENFLOW_SQLSERVER_CDC": "mssql",
    "OPENFLOW_KAFKA": "kafka",
}


# --- Container keys -------------------------------------------------------


class OpenflowDeploymentKey(ContainerKey):
    deployment: str


class OpenflowRuntimeKey(OpenflowDeploymentKey):
    runtime: str


# --- Config-derived lineage ---------------------------------------------


@dataclasses.dataclass
class OpenflowLineage:
    source_database: Optional[str] = None
    source_tables: List[Tuple[str, str]] = dataclasses.field(default_factory=list)
    table_pattern: Optional[str] = None
    destination_database: Optional[str] = None
    schema_strategy: Optional[str] = None
    unparseable_tables: List[str] = dataclasses.field(default_factory=list)
    # Populated when the Source section carried none of the candidate URL keys, so the
    # caller can report which keys it DID see rather than silently emitting no upstream.
    unrecognised_source_url_keys: List[str] = dataclasses.field(default_factory=list)


def _parse_table_names(raw: str) -> Tuple[List[Tuple[str, str]], List[str]]:
    # Returns (parsed, unparseable). The second element exists so the caller can
    # report dropped entries: an entry with no schema qualifier silently vanishing
    # from lineage is indistinguishable from a connector that legitimately has no
    # source tables, and losing one table out of ten is exactly the kind of
    # partial-lineage failure nothing else in the pipeline would surface.
    tables: List[Tuple[str, str]] = []
    unparseable: List[str] = []
    for entry in raw.split(","):
        cleaned = entry.strip().replace('"', "")
        if not cleaned:
            continue
        if "." not in cleaned:
            unparseable.append(cleaned)
            continue
        schema, _, table = cleaned.rpartition(".")
        tables.append((schema, table))
    return tables, unparseable


def parse_connector_config(config_json: Dict[str, Any]) -> OpenflowLineage:
    # Iterate EVERY section. Descending into configuration[0] only finds the
    # destination on connectors that happen to list it first.
    lineage = OpenflowLineage()
    for section in config_json.get("configuration") or []:
        name = section.get("name")
        properties = section.get("properties") or {}
        if name == SECTION_SOURCE:
            source_url = next(
                (
                    properties[key]
                    for key in PROP_SOURCE_URL_CANDIDATES
                    if properties.get(key)
                ),
                None,
            )
            if source_url:
                # jdbc:postgresql://host:5432/appdb -> appdb
                path = urlparse(source_url[len("jdbc:") :]).path
                lineage.source_database = path.lstrip("/") or None
            else:
                lineage.unrecognised_source_url_keys = sorted(properties)
        elif name == SECTION_REPLICATION:
            names = properties.get(PROP_INCLUDED_TABLE_NAMES)
            if names:
                lineage.source_tables, lineage.unparseable_tables = _parse_table_names(
                    names
                )
            lineage.table_pattern = properties.get(PROP_INCLUDED_TABLE_PATTERN)
        elif name == SECTION_DESTINATION:
            lineage.destination_database = properties.get(PROP_DESTINATION_DATABASE)
            lineage.schema_strategy = properties.get(PROP_SCHEMA_STRATEGY)
    return lineage


def destination_identifier(
    destination_database: str,
    source_schema: str,
    source_table: str,
    schema_strategy: Optional[str],
) -> Optional[str]:
    # Only SOURCE_SCHEMA is implemented. Prefix/Suffix/Pattern strategies exist;
    # guessing one produces a well-formed URN naming a table that is not there,
    # which no layer reports as an error.
    #
    # This is the single formula for the destination's dotted identifier: the
    # caller feeds the returned string straight into the URN (after case
    # folding) rather than recomputing it, so a future Prefix/Suffix strategy
    # only ever needs a change here.
    if schema_strategy != SCHEMA_STRATEGY_SOURCE_SCHEMA:
        return None
    return f"{destination_database}.{source_schema}.{source_table}"


# --- Connector DataFlow / DataJob -------------------------------------------


def _connector_properties(connector: OpenflowConnector) -> Dict[str, str]:
    properties: Dict[str, str] = {}
    if connector.connector_id:
        properties["connector_id"] = connector.connector_id
    if connector.connector_definition:
        properties["connector_definition"] = connector.connector_definition
    if connector.default_version:
        properties["default_version"] = connector.default_version
    if connector.status:
        properties["status"] = connector.status
    if connector.runtime_name:
        properties["runtime"] = connector.runtime_name
    return properties


def build_connector_flow(
    connector: OpenflowConnector,
    platform_instance: Optional[str],
    env: str,
) -> DataFlow:
    # Keyed on the COMPOSITE <runtime_name>/<connector_name> (connector.key), not on
    # CONNECTOR_ID and not on the bare name. Three measured facts force this:
    #   - SHOW OPENFLOW CONNECTORS returns no id column, so CONNECTOR_ID is not
    #     available for every connector and cannot be the identity.
    #   - The ACCOUNT_USAGE views lag ~20 min, so an id-keyed URN would change
    #     identity once the view caught up, producing two entities for one connector.
    #   - Snowsight allows several connectors to share a display name, so the bare
    #     name collides across runtimes.
    # CONNECTOR_ID is carried in custom properties instead.
    return DataFlow(
        name=connector.key,
        platform=PLATFORM,
        platform_instance=platform_instance,
        env=env,
        display_name=connector.display_name or connector.name,
        subtype=DataFlowSubTypes.OPENFLOW_CONNECTOR,
        custom_properties=_connector_properties(connector),
    )


def build_connector_job(
    connector: OpenflowConnector,
    flow: DataFlow,
    inlets: Sequence[str],
    outlets: Sequence[str],
) -> DataJob:
    return DataJob(
        name=connector.key,
        flow=flow,
        display_name=connector.display_name or connector.name,
        subtype=DataJobSubTypes.OPENFLOW_CONNECTOR_SYNC,
        custom_properties=_connector_properties(connector),
        inlets=list(inlets),
        outlets=list(outlets),
    )


# --- Source -----------------------------------------------------------------


@platform_name("Snowflake Openflow", id="snowflake-openflow")
@config_class(SnowflakeOpenflowSourceConfig)
@support_status(SupportStatus.ALPHA)
@capability(SourceCapability.PLATFORM_INSTANCE, "Enabled by default")
@capability(
    SourceCapability.CONTAINERS,
    "Enabled by default",
    subtype_modifier=[
        SourceCapabilityModifier.OPENFLOW_DEPLOYMENT,
        SourceCapabilityModifier.OPENFLOW_RUNTIME,
    ],
)
@capability(
    SourceCapability.LINEAGE_COARSE,
    "Derived from each connector's own configuration; disable with "
    "`include_openflow_lineage: false`",
)
@capability(SourceCapability.OWNERSHIP, "Extracted from each object's OWNER")
@capability(
    SourceCapability.DELETION_DETECTION,
    "Enabled by default via stateful ingestion, using DELETED_ON from the "
    "ACCOUNT_USAGE views",
    supported=True,
)
@capability(SourceCapability.TEST_CONNECTION, "Enabled by default")
@capability(SourceCapability.SCHEMA_METADATA, SNOWFLAKE_SOURCE_HINT, supported=False)
@capability(
    SourceCapability.LINEAGE_FINE,
    "Not supported: NiFi processors operate on FlowFiles rather than typed SQL, so "
    "there is no statement to parse for column mapping.",
    supported=False,
)
@capability(SourceCapability.DATA_PROFILING, SNOWFLAKE_SOURCE_HINT, supported=False)
@capability(
    SourceCapability.USAGE_STATS,
    "Not supported: OPENFLOW_USAGE_HISTORY reports credit consumption, not dataset "
    "usage. Connector run history is available separately via `include_run_history`.",
    supported=False,
)
@capability(
    SourceCapability.TAGS,
    "Not supported: the Openflow object views expose no tag column.",
    supported=False,
)
@capability(
    SourceCapability.DOMAINS,
    "Not supported: domains are assigned in DataHub rather than sourced from Openflow.",
    supported=False,
)
class SnowflakeOpenflowSource(StatefulIngestionSourceBase, TestableSource):
    def __init__(
        self, config: SnowflakeOpenflowSourceConfig, ctx: PipelineContext
    ) -> None:
        super().__init__(config, ctx)
        self.config = config
        self.platform = PLATFORM
        self.report: SnowflakeOpenflowReport = SnowflakeOpenflowReport()
        self.connection: SnowflakeConnection = config.connection.get_connection()

        # Stale entity removal needs NO wiring here — AutoStaleEntityRemovalProcessor
        # (api/source.py) enables itself from self.state_provider (always set by
        # StatefulIngestionSourceBase) and this report being a
        # StaleEntityRemovalSourceReport.

    @classmethod
    def create(
        cls, config_dict: Dict[str, Any], ctx: PipelineContext
    ) -> "SnowflakeOpenflowSource":
        config = SnowflakeOpenflowSourceConfig.model_validate(config_dict)
        return cls(config, ctx)

    def get_report(self) -> SnowflakeOpenflowReport:
        return self.report

    def close(self) -> None:
        self.connection.close()
        super().close()

    @staticmethod
    def test_connection(config_dict: Dict[str, Any]) -> TestConnectionReport:
        report = TestConnectionReport()
        try:
            # Config parse and connect share one handler: neither is the
            # visibility probe below, and either failing here means there is
            # no connection to attribute a capability failure to.
            config = SnowflakeOpenflowSourceConfig.model_validate(config_dict)
            connection = config.connection.get_connection()
        except Exception as exc:
            report.basic_connectivity = CapabilityReport(
                capable=False, failure_reason=str(exc)
            )
            return report
        report.basic_connectivity = CapabilityReport(capable=True)
        try:
            # A successful connection says nothing about Openflow visibility,
            # which is granted per object. Probe it separately so a role missing
            # MONITOR is reported here rather than as an empty ingestion. The
            # probe itself erroring (a permission error, a transient failure) is
            # caught below rather than escaping, and reported distinctly from
            # the zero-rows case: both are "not capable", but only the latter
            # names MONITOR.
            rows = list(connection.query(SnowflakeOpenflowQuery.show_deployments()))
            report.capability_report = {
                SourceCapability.CONTAINERS: CapabilityReport(
                    capable=bool(rows),
                    failure_reason=None
                    if rows
                    else "No Openflow deployments are visible to this role. Grant "
                    "MONITOR on the deployments and runtimes to ingest.",
                )
            }
        except Exception as exc:
            report.capability_report = {
                SourceCapability.CONTAINERS: CapabilityReport(
                    capable=False, failure_reason=str(exc)
                )
            }
        finally:
            connection.close()
        return report

    def _deployment_key(self, deployment: OpenflowDeployment) -> OpenflowDeploymentKey:
        return OpenflowDeploymentKey(
            platform=self.platform,
            instance=self.config.platform_instance,
            env=self.config.env,
            deployment=deployment.key,
        )

    def _runtime_key(
        self, runtime: OpenflowRuntime, deployment: OpenflowDeployment
    ) -> OpenflowRuntimeKey:
        return OpenflowRuntimeKey(
            platform=self.platform,
            instance=self.config.platform_instance,
            env=self.config.env,
            deployment=deployment.key,
            runtime=runtime.key,
        )

    def _query_rows(self, query: str) -> List[Dict[str, Any]]:
        return [dict(row) for row in self.connection.query(query)]

    def _read_connector_config(
        self, connector: OpenflowConnector
    ) -> Optional[Dict[str, Any]]:
        if not connector.version_location_uri:
            return None
        try:
            # The URI is used exactly as the row reports it; substituting a
            # version segment fails with errno 99112.
            rows = self._query_rows(
                SnowflakeOpenflowQuery.get_stage_file(
                    connector.version_location_uri, CONFIG_FILENAME
                )
            )
            if not rows:
                return None
            # `SELECT $1 FROM '<uri>config.json'` is assumed to return the whole
            # file as a single row and column -- unverified against a live
            # account, since credentials are unavailable in this environment.
            # The row access and the JSON parse both stay inside this try: a
            # row shaped differently than expected (e.g. an empty row) or
            # invalid JSON must degrade this one connector's lineage to a
            # warning, exactly like a stage READ failure, rather than crash
            # the whole run.
            content = next(iter(rows[0].values()))
            return json.loads(content)
        except Exception as exc:
            self.report.num_config_reads_failed += 1
            self.report.warning(
                title="Could not read connector configuration",
                message="Lineage for this connector is skipped. This can mean the "
                "role lacks READ on the connector's version stage, or the stage "
                "file was not the expected single-row JSON document.",
                context=connector.key,
                exc=exc,
            )
            return None

    def _lineage_for_connector(
        self, connector: OpenflowConnector
    ) -> Tuple[List[str], List[str]]:
        config_json = self._read_connector_config(connector)
        if config_json is None:
            return [], []
        lineage = parse_connector_config(config_json)
        if lineage.unparseable_tables:
            # Reuse num_lineage_edges_skipped rather than adding a counter: the
            # operator-visible fact is the same, an edge we could not build.
            self.report.num_lineage_edges_skipped += len(lineage.unparseable_tables)
            self.report.warning(
                title="Unparseable source table name",
                message="These entries carried no schema qualifier, so no upstream "
                "table could be derived and their lineage is omitted.",
                context=f"{connector.key}: {lineage.unparseable_tables}",
            )
        if lineage.unrecognised_source_url_keys:
            self.report.warning(
                title="Source connection URL property not recognised",
                message="The connector's Source section carried none of the known "
                "connection-URL property names, so no upstream dataset could be "
                "derived. Downstream lineage is still emitted.",
                context=f"{connector.key}: {lineage.unrecognised_source_url_keys}",
            )
        if not lineage.destination_database:
            return [], []
        if lineage.table_pattern and not lineage.source_tables:
            self.report.num_connectors_without_enumerable_tables += 1
            return [], []

        identifiers = SnowflakeIdentifierBuilder(
            identifier_config=self.config.get_snowflake_identifier_config(),
            structured_reporter=self.report,
        )
        upstream_platform = CONNECTOR_DEFINITION_PLATFORM.get(
            connector.connector_definition or ""
        )
        inlets: List[str] = []
        outlets: List[str] = []
        for source_schema, source_table in lineage.source_tables:
            destination = destination_identifier(
                lineage.destination_database,
                source_schema,
                source_table,
                lineage.schema_strategy,
            )
            if destination is None:
                self.report.num_lineage_edges_skipped += 1
                self.report.warning(
                    title="Unrecognised destination schema strategy",
                    message="This Destination Schema Strategy is not implemented, so "
                    "the destination table cannot be derived. Lineage is skipped "
                    "rather than guessed.",
                    context=f"{connector.key}: strategy={lineage.schema_strategy!r}",
                )
                continue
            # `destination` (from destination_identifier, above) is the single
            # formula for the destination's dotted identifier -- it is fed
            # straight into the URN rather than recomputed via
            # get_dataset_identifier's own db/schema/table formula, so that
            # adding a Prefix/Suffix strategy later only ever needs a change
            # in one place.
            outlets.append(
                identifiers.gen_dataset_urn(
                    identifiers.snowflake_identifier(destination)
                )
            )
            if upstream_platform and lineage.source_database:
                inlets.append(
                    make_dataset_urn_with_platform_instance(
                        platform=upstream_platform,
                        name=f"{lineage.source_database}.{source_schema}.{source_table}",
                        platform_instance=None,
                        env=self.config.env,
                    )
                )
            self.report.num_lineage_edges += 1
        return inlets, outlets

    def _paged_history(
        self, builder: Callable[[Optional[str]], str]
    ) -> List[Dict[str, Any]]:
        # Cursor pagination on CREATED_ON. Three things here are deliberate; the
        # first two are guards against defects demonstrated before this was written.
        rows: List[Dict[str, Any]] = []
        cursor: Optional[str] = None
        pages = 0
        while True:
            page = self._query_rows(builder(cursor))
            if not page:
                break
            rows.extend(page)
            pages += 1
            if len(page) < SnowflakeOpenflowQuery.PAGE_SIZE:
                break

            next_created_on = page[-1].get("CREATED_ON")
            if next_created_on is None:
                # Guard 1: a NULL CREATED_ON on the page boundary would make the
                # cursor the literal string "None", and the next predicate
                # `WHERE CREATED_ON > 'None'` is nonsense rather than an error.
                self.report.warning(
                    title="Cannot paginate past a NULL CREATED_ON",
                    message="A full page ended with a row whose CREATED_ON is NULL, "
                    "so the cursor cannot advance. Results may be incomplete.",
                )
                break
            next_cursor = str(next_created_on)
            if next_cursor == cursor:
                # Guard 2: every row on a full page shares one timestamp, so the
                # cursor cannot advance without skipping the whole tie group.
                # Stopping beats looping forever on the same page.
                self.report.warning(
                    title="Pagination stalled on identical timestamps",
                    message="A full page of rows shares a single CREATED_ON, so the "
                    "cursor cannot advance. Results may be incomplete.",
                )
                break
            cursor = next_cursor

        if pages > 1:
            # Known limitation, surfaced only if pagination actually engages.
            # `CREATED_ON >` loses rows whose timestamp is exactly the page
            # boundary and did not fit on the page. At documented volumes (low
            # hundreds of objects against PAGE_SIZE 1000) a single page always
            # suffices, so this stays unreachable -- but if scale changes, the
            # operator finds out here rather than from missing entities.
            self.report.warning(
                title="Openflow history required more than one page",
                message="Rows sharing the exact CREATED_ON of a page boundary can be "
                "skipped by the strict-greater-than cursor. Verify counts if entities "
                "appear to be missing.",
                context=f"pages={pages}",
            )
        return rows

    def _fetch_deployments(self) -> List[OpenflowDeployment]:
        show = [
            OpenflowDeployment.from_row(row)
            for row in self._query_rows(SnowflakeOpenflowQuery.show_deployments())
        ]
        history = [
            OpenflowDeployment.from_row(row)
            for row in self._paged_history(SnowflakeOpenflowQuery.deployment_history)
        ]
        merged = merge_show_and_history(
            [row for row in show if row], [row for row in history if row]
        )
        live = [row for row in merged if row.deleted_on is None]
        if not live:
            self.report.report_empty_inventory("deployments")
        return [
            row
            for row in live
            if self._allowed(
                self.config.deployment_pattern,
                row.name or row.key,
                self.report.report_dropped_deployment,
            )
        ]

    def _fetch_runtimes(self) -> List[OpenflowRuntime]:
        show = [
            OpenflowRuntime.from_row(row)
            for row in self._query_rows(SnowflakeOpenflowQuery.show_runtimes())
        ]
        history = [
            OpenflowRuntime.from_row(row)
            for row in self._paged_history(SnowflakeOpenflowQuery.runtime_history)
        ]
        merged = merge_show_and_history(
            [row for row in show if row], [row for row in history if row]
        )
        live = [row for row in merged if row.deleted_on is None]
        if not live:
            self.report.report_empty_inventory("runtimes")
        return [
            row
            for row in live
            if self._allowed(
                self.config.runtime_pattern,
                row.name or row.key,
                self.report.report_dropped_runtime,
            )
        ]

    def _fetch_connectors(self) -> List[OpenflowConnector]:
        show = [
            OpenflowConnector.from_row(row)
            for row in self._query_rows(SnowflakeOpenflowQuery.show_connectors())
        ]
        history = [
            OpenflowConnector.from_row(row)
            for row in self._paged_history(SnowflakeOpenflowQuery.connector_history)
        ]
        merged = merge_show_and_history(
            [row for row in show if row], [row for row in history if row]
        )
        live = [row for row in merged if row.deleted_on is None]
        if not live:
            # Gen 1 connectors are not SQL objects at all, so this surface sees
            # only Gen 2. An account running Gen 1 exclusively looks empty here
            # and the count of omitted Gen 1 connectors is not observable.
            self.report.report_empty_inventory("connectors")
        return [
            row
            for row in live
            if self._allowed(
                self.config.connector_pattern,
                row.name or row.key,
                self.report.report_dropped_connector,
            )
        ]

    @staticmethod
    def _allowed(
        pattern: AllowDenyPattern, name: str, on_dropped: Callable[[str], None]
    ) -> bool:
        if pattern.allowed(name):
            return True
        on_dropped(name)
        return False

    def get_workunits_internal(self) -> Iterable[MetadataWorkUnit]:
        deployments = self._fetch_deployments()
        runtimes = self._fetch_runtimes()
        by_deployment_name = {deployment.name: deployment for deployment in deployments}

        # Parents before children so auto_browse_path_v2 can build browse paths.
        for deployment in deployments:
            self.report.num_deployments += 1
            yield from gen_containers(
                container_key=self._deployment_key(deployment),
                name=deployment.display_name or deployment.name or deployment.key,
                sub_types=[GenericContainerSubTypes.OPENFLOW_DEPLOYMENT],
                owner_urn=None,
                extra_properties=self._deployment_properties(deployment),
            )

        for runtime in runtimes:
            parent_deployment = by_deployment_name.get(runtime.deployment_name)
            if parent_deployment is None:
                self.report.warning(
                    title="Runtime with no visible parent deployment",
                    message="The runtime's deployment is not visible to this role, so "
                    "the runtime container cannot be nested. Grant MONITOR on the "
                    "deployment.",
                    context=runtime.key,
                )
                continue
            self.report.num_runtimes += 1
            yield from gen_containers(
                container_key=self._runtime_key(runtime, parent_deployment),
                name=runtime.display_name or runtime.name or runtime.key,
                sub_types=[GenericContainerSubTypes.OPENFLOW_RUNTIME],
                parent_container_key=self._deployment_key(parent_deployment),
                extra_properties=self._runtime_properties(runtime),
            )

        for connector in self._fetch_connectors():
            self.report.num_connectors += 1
            flow = build_connector_flow(
                connector,
                platform_instance=self.config.platform_instance,
                env=self.config.env,
            )
            yield from flow.as_workunits()
            inlets: List[str] = []
            outlets: List[str] = []
            if self.config.include_openflow_lineage:
                inlets, outlets = self._lineage_for_connector(connector)
            # The job is emitted regardless of whether lineage was found, so run
            # history and connector metadata have a stable anchor.
            job = build_connector_job(connector, flow, inlets=inlets, outlets=outlets)
            yield from job.as_workunits()

    @staticmethod
    def _deployment_properties(deployment: OpenflowDeployment) -> Dict[str, str]:
        properties = {"deployment_key": deployment.key}
        if deployment.status:
            properties["status"] = deployment.status
        return properties

    @staticmethod
    def _runtime_properties(runtime: OpenflowRuntime) -> Dict[str, str]:
        properties = {"runtime_key": runtime.key}
        if runtime.status:
            properties["status"] = runtime.status
        if runtime.execute_as_role:
            properties["execute_as_role"] = runtime.execute_as_role
        if runtime.object_database and runtime.object_schema:
            # The runtime OBJECT's own location. Explicitly not a data destination.
            properties["object_location"] = (
                f"{runtime.object_database}.{runtime.object_schema}"
            )
        return properties

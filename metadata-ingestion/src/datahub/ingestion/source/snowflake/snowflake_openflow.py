import logging
from typing import Any, Callable, Dict, Iterable, List, Optional

from datahub.configuration.common import AllowDenyPattern
from datahub.emitter.mcp_builder import ContainerKey, gen_containers
from datahub.ingestion.api.common import PipelineContext
from datahub.ingestion.api.workunit import MetadataWorkUnit
from datahub.ingestion.source.common.subtypes import GenericContainerSubTypes
from datahub.ingestion.source.snowflake.snowflake_connection import SnowflakeConnection
from datahub.ingestion.source.snowflake.snowflake_openflow_config import (
    SnowflakeOpenflowSourceConfig,
)
from datahub.ingestion.source.snowflake.snowflake_openflow_models import (
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
from datahub.ingestion.source.state.stateful_ingestion_base import (
    StatefulIngestionSourceBase,
)

logger = logging.getLogger(__name__)

PLATFORM = "openflow"


# --- Container keys -------------------------------------------------------


class OpenflowDeploymentKey(ContainerKey):
    deployment: str


class OpenflowRuntimeKey(OpenflowDeploymentKey):
    runtime: str


# --- Source -----------------------------------------------------------------


class SnowflakeOpenflowSource(StatefulIngestionSourceBase):
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

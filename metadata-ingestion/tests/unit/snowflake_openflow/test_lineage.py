import gzip
import json
import pathlib
from typing import Any, Callable, Dict, List, Optional

import pytest

from datahub.ingestion.source.snowflake.snowflake_openflow import (
    CONFIG_FILENAME,
    SCHEMA_STRATEGY_SOURCE_SCHEMA,
    SnowflakeOpenflowSource,
    destination_identifier,
    parse_connector_config,
    property_value,
)
from datahub.ingestion.source.snowflake.snowflake_openflow_config import (
    SnowflakeOpenflowSourceConfig,
)
from datahub.ingestion.source.snowflake.snowflake_openflow_models import (
    OpenflowConnector,
)
from datahub.ingestion.source.snowflake.snowflake_openflow_query import (
    CONNECTOR_HISTORY,
    DEPLOYMENT_HISTORY,
    RUNTIME_HISTORY,
    SnowflakeOpenflowQuery,
)
from datahub.ingestion.source.snowflake.snowflake_openflow_report import (
    SnowflakeOpenflowReport,
)


def _wrap(value: Optional[str], value_type: str = "STRING_LITERAL") -> Dict[str, Any]:
    # Mirrors Openflow's real property wrapper: {"valueType": ..., "value": ...}.
    # An unset property (value=None) omits the "value" key entirely, matching
    # how a real connector represents e.g. an unused Destination Schema Suffix
    # -- not `{"value": null}` and not `{"value": ""}`.
    wrapped: Dict[str, Any] = {"valueType": value_type}
    if value is not None:
        wrapped["value"] = value
    return wrapped


CONFIG_JSON = {
    "configuration": [
        {
            "name": "Source",
            "properties": {
                "JDBC URL": _wrap("jdbc:postgresql://host:5432/appdb"),
                "Postgres Username": _wrap("repl"),
            },
        },
        {
            "name": "Replication table schema",
            "properties": {
                "Included Comma Separated Source Table Names": _wrap(
                    '"public"."testtable"'
                )
            },
        },
        {
            "name": "Destination details",
            "properties": {
                "Snowflake Destination Database": _wrap("OPENFLOW_DEV"),
                "Destination Schema Strategy": _wrap("SOURCE_SCHEMA"),
                "Object Identifier Resolution": _wrap("CASE_INSENSITIVE"),
            },
        },
    ]
}


def test_parses_source_tables_and_destination():
    lineage = parse_connector_config(CONFIG_JSON)
    assert lineage.source_tables == [("public", "testtable")]
    assert lineage.destination_database == "OPENFLOW_DEV"
    assert lineage.schema_strategy == SCHEMA_STRATEGY_SOURCE_SCHEMA
    assert lineage.source_database == "appdb"


def test_walks_every_configuration_section_not_just_the_first():
    # An early implementation descended only into configuration[0] and silently
    # found no destination at all.
    shuffled = {"configuration": list(reversed(CONFIG_JSON["configuration"]))}
    lineage = parse_connector_config(shuffled)
    assert lineage.destination_database == "OPENFLOW_DEV"
    assert lineage.source_tables == [("public", "testtable")]


def test_source_schema_strategy_maps_schema_through():
    identifier = destination_identifier(
        destination_database="OPENFLOW_DEV",
        source_schema="public",
        source_table="testtable",
        schema_strategy=SCHEMA_STRATEGY_SOURCE_SCHEMA,
    )
    assert identifier == "OPENFLOW_DEV.public.testtable"


def test_unrecognised_schema_strategy_returns_none_rather_than_guessing():
    # Prefix/Suffix/Pattern strategies exist. A guess here produces a
    # well-formed URN pointing at a table that does not exist.
    assert (
        destination_identifier(
            destination_database="OPENFLOW_DEV",
            source_schema="public",
            source_table="testtable",
            schema_strategy="SOME_FUTURE_STRATEGY",
        )
        is None
    )


def test_pattern_configured_connector_yields_no_enumerable_tables():
    config = {
        "configuration": [
            {
                "name": "Replication table schema",
                "properties": {"Included Source Table Pattern": _wrap("public\\..*")},
            },
            {
                "name": "Destination details",
                "properties": {
                    "Snowflake Destination Database": _wrap("OPENFLOW_DEV"),
                    "Destination Schema Strategy": _wrap("SOURCE_SCHEMA"),
                },
            },
        ]
    }
    lineage = parse_connector_config(config)
    assert lineage.source_tables == []
    assert lineage.table_pattern == "public\\..*"


def test_source_url_uses_the_observed_property_name():
    # Probe Result 37 recorded the real name as "Source Database Connection URL".
    # An earlier draft looked for "JDBC URL", which no observed connector uses, so
    # source_database stayed None and no upstream was ever emitted -- silently.
    config = {
        "configuration": [
            {
                "name": "Source",
                "properties": {
                    "Source Database Connection URL": _wrap(
                        "jdbc:postgresql://host:5432/appdb"
                    ),
                    "Source Database User": _wrap("repl"),
                },
            }
        ]
    }
    assert parse_connector_config(config).source_database == "appdb"


def test_unrecognised_source_url_key_is_reported_not_silently_ignored():
    config = {
        "configuration": [
            {
                "name": "Source",
                "properties": {"Some Future Url Property": _wrap("jdbc:x://h/db")},
            }
        ]
    }
    lineage = parse_connector_config(config)
    assert lineage.source_database is None
    assert lineage.unrecognised_source_url_keys == ["Some Future Url Property"]


def test_unqualified_table_name_is_reported_not_silently_dropped():
    # An entry with no schema qualifier must be surfaced. Dropping it silently
    # makes a connector with 9 of 10 tables look identical to one with all 10.
    config = {
        "configuration": [
            {
                "name": "Replication table schema",
                "properties": {
                    "Included Comma Separated Source Table Names": _wrap(
                        '"public"."a",noschema'
                    )
                },
            }
        ]
    }
    lineage = parse_connector_config(config)
    assert lineage.source_tables == [("public", "a")]
    assert lineage.unparseable_tables == ["noschema"]


@pytest.mark.parametrize(
    "raw,expected",
    [
        ('"public"."testtable"', [("public", "testtable")]),
        ('"public"."a","public"."b"', [("public", "a"), ("public", "b")]),
        ("public.testtable", [("public", "testtable")]),
        ("", []),
    ],
)
def test_table_name_list_parsing(raw, expected):
    config = {
        "configuration": [
            {
                "name": "Replication table schema",
                "properties": {
                    "Included Comma Separated Source Table Names": _wrap(raw)
                },
            }
        ]
    }
    assert parse_connector_config(config).source_tables == expected


# --- property_value: the wrapper accessor. A live TypeError (unhashable ------
# --- type: 'slice') is what surfaced that every property is wrapped as -------
# --- {"valueType": ..., "value": ...} rather than a bare string. -------------


def test_property_value_reads_the_wrapped_string():
    properties = {"Snowflake Destination Database": _wrap("OPENFLOW_DEV")}
    assert (
        property_value(properties, "Snowflake Destination Database") == "OPENFLOW_DEV"
    )


def test_property_value_unset_property_reads_as_none():
    # Openflow marks an unset property by omitting "value" entirely -- not
    # null, not "" -- exactly how a real connector's unused Included Source
    # Table Pattern and Destination Schema Suffix both look.
    properties = {"Included Source Table Pattern": _wrap(None)}
    assert property_value(properties, "Included Source Table Pattern") is None


def test_property_value_ignores_non_literal_value_types():
    # ASSET_REFERENCE and SECRET_REFERENCE properties carry assetIds /
    # fullyQualifiedSecretName instead of "value". Reaching into one for a
    # string would silently pick up whatever happened to be under a "value"
    # key, or crash when there isn't one.
    properties = {
        "Source Database Driver": {
            "valueType": "ASSET_REFERENCE",
            "assetIds": ["postgresql-42.7.13-2.jar"],
        }
    }
    assert property_value(properties, "Source Database Driver") is None


def test_property_value_missing_key_reads_as_none():
    assert property_value({}, "Anything") is None


def test_property_value_tolerates_a_bare_string_for_forward_compatibility():
    # Defensive: every observed property is wrapped, but a future config
    # format version could flatten one to a bare string.
    properties = {"Snowflake Destination Database": "OPENFLOW_DEV"}
    assert (
        property_value(properties, "Snowflake Destination Database") == "OPENFLOW_DEV"
    )


def test_parses_the_real_observed_wrapped_config_shape():
    # The real config.json shape (Round 13 probe, corrected): every property is
    # {"valueType": ..., "value": ...}, with "value" entirely absent when unset.
    # CONFIG_JSON above, using bare strings, is a placeholder fixture that never
    # actually occurs -- this is the real file, in its real shape, asserting
    # the real answer.
    config = {
        "configuration": [
            {
                "name": "Source",
                "properties": {
                    "Source Database Connection URL": _wrap(
                        "jdbc:postgresql://host:5432/postgres?sslmode=require"
                    ),
                    "Source Database Publication Name": _wrap("openflow_pub"),
                    "Source Database Driver": {
                        "valueType": "ASSET_REFERENCE",
                        "assetIds": ["postgresql-42.7.13-2.jar"],
                    },
                },
            },
            {
                "name": "Replication table schema",
                "properties": {
                    "Included Comma Separated Source Table Names": _wrap(
                        '"public"."testtable"'
                    ),
                    "Included Source Table Pattern": _wrap(None),
                },
            },
            {
                "name": "Destination details",
                "properties": {
                    "Snowflake Destination Database": _wrap("OPENFLOW_DEV"),
                    "Destination Schema Strategy": _wrap("SOURCE_SCHEMA"),
                    "Destination Schema Suffix": _wrap(None),
                    "Table Storage Format": _wrap("STANDARD"),
                },
            },
        ]
    }

    lineage = parse_connector_config(config)

    assert lineage.source_database == "postgres"
    assert lineage.source_tables == [("public", "testtable")]
    assert lineage.destination_database == "OPENFLOW_DEV"
    assert lineage.schema_strategy == "SOURCE_SCHEMA"
    assert lineage.table_pattern is None


# --- _lineage_for_connector / _read_connector_config: the orchestration ----
# --- layer that turns parsed config into report calls and URNs. Driven ----
# --- through the same _query_rows seam test_source.py uses -- no network. --
# --- _read_connector_config issues GET, whose real side effect is writing a
# --- file into the local directory named in the query's 'file://<dir>'
# --- argument -- so the fake below reproduces exactly that side effect
# --- rather than returning file content from _query_rows itself.

MINIMAL_CONNECTION = {
    "connection": {
        "account_id": "abc12345",
        "username": "user",
        "password": "pass",
    }
}


def _make_source(**config_overrides: Any) -> SnowflakeOpenflowSource:
    config = SnowflakeOpenflowSourceConfig.model_validate(
        {**MINIMAL_CONNECTION, **config_overrides}
    )
    source = object.__new__(SnowflakeOpenflowSource)
    source.config = config
    source.platform = "openflow"
    source.report = SnowflakeOpenflowReport()
    return source


def _connector(
    version_location_uri: Optional[str] = "@stage/v1/",
) -> OpenflowConnector:
    return OpenflowConnector(
        name="pg_cdc",
        runtime_name="IngestionTest",
        connector_id="1",
        connector_definition="OPENFLOW_POSTGRES_CDC",
        version_location_uri=version_location_uri,
    )


def _fake_get(content: Optional[bytes]) -> Callable[[str], List[Dict[str, Any]]]:
    # Real GET writes a file into the local_dir named in the query string
    # (`... 'file://<local_dir>'`) and returns audit-trail rows, not content.
    # content=None simulates GET reporting success while placing no file --
    # not observed against a live account, but the code must not crash on it.
    def fake(query: str) -> List[Dict[str, Any]]:
        local_dir = query.rsplit("'file://", 1)[1].rstrip("'")
        if content is not None:
            (pathlib.Path(local_dir) / CONFIG_FILENAME).write_bytes(content)
        return [{"file": CONFIG_FILENAME, "status": "DOWNLOADED"}]

    return fake


def _warning_titles(report: SnowflakeOpenflowReport) -> List[Optional[str]]:
    return [entry.title for entry in report.warnings]


def test_lineage_for_connector_happy_path_returns_inlets_and_outlets():
    source = _make_source()
    connector = _connector()
    source._query_rows = _fake_get(json.dumps(CONFIG_JSON).encode())  # type: ignore[assignment]

    inlets, outlets = source._lineage_for_connector(connector)

    assert outlets == [
        "urn:li:dataset:(urn:li:dataPlatform:snowflake,openflow_dev.public.testtable,PROD)"
    ]
    assert inlets == [
        "urn:li:dataset:(urn:li:dataPlatform:postgres,appdb.public.testtable,PROD)"
    ]
    assert source.report.num_lineage_edges == 1
    assert source.report.num_lineage_edges_skipped == 0


def test_lineage_for_connector_handles_gzip_compressed_config():
    # Whether a staged file arrives gzip-compressed depends on Snowflake's
    # AUTO_COMPRESS staging behaviour, not on anything this connector
    # controls. Detected via the gzip magic bytes, not a ".gz" filename
    # suffix, since GET's own suffix convention is not a guarantee.
    source = _make_source()
    connector = _connector()
    compressed = gzip.compress(json.dumps(CONFIG_JSON).encode())
    source._query_rows = _fake_get(compressed)  # type: ignore[assignment]

    inlets, outlets = source._lineage_for_connector(connector)

    assert outlets == [
        "urn:li:dataset:(urn:li:dataPlatform:snowflake,openflow_dev.public.testtable,PROD)"
    ]
    assert inlets == [
        "urn:li:dataset:(urn:li:dataPlatform:postgres,appdb.public.testtable,PROD)"
    ]
    assert source.report.num_config_reads_failed == 0


def test_lineage_for_connector_reports_config_read_failure():
    # A raising _query_rows -- e.g. missing READ on the version stage.
    source = _make_source()
    connector = _connector()

    def raise_error(query: str) -> List[Dict[str, Any]]:
        raise RuntimeError("boom")

    source._query_rows = raise_error  # type: ignore[method-assign]

    inlets, outlets = source._lineage_for_connector(connector)

    assert inlets == []
    assert outlets == []
    assert source.report.num_config_reads_failed == 1
    assert "Could not read connector configuration" in _warning_titles(source.report)


def test_lineage_for_connector_reports_malformed_json_as_config_read_failure():
    # The exact shape hit in production: the earlier SELECT $1 FROM stage
    # approach parsed the file under Snowflake's default CSV file format, so
    # $1 was only the text up to the first comma -- 24 of 2921 real bytes,
    # ending mid-field as `{"configFormatVersion":1`. GET fixes the read
    # itself, but json.loads must still stay inside the same try/except as
    # the download: invalid content must degrade to a per-connector warning,
    # not crash the whole ingestion run the way an uncaught JSONDecodeError
    # inside get_workunits_internal's generator would.
    source = _make_source()
    connector = _connector()
    truncated = b'{"configFormatVersion":1'
    source._query_rows = _fake_get(truncated)  # type: ignore[assignment]

    inlets, outlets = source._lineage_for_connector(connector)

    assert inlets == []
    assert outlets == []
    assert source.report.num_config_reads_failed == 1
    assert "Could not read connector configuration" in _warning_titles(source.report)


def test_lineage_for_connector_reports_missing_download_as_config_read_failure():
    # GET reporting success while placing no file in local_dir is not a shape
    # confirmed against a live account, but nothing rules it out either
    # (a permission edge case, a stage inconsistency). It must degrade to the
    # same warning as any other failed read, not raise out of the generator
    # that drives ingestion.
    source = _make_source()
    connector = _connector()
    source._query_rows = _fake_get(None)  # type: ignore[assignment]

    inlets, outlets = source._lineage_for_connector(connector)

    assert inlets == []
    assert outlets == []
    assert source.report.num_config_reads_failed == 1
    assert "Could not read connector configuration" in _warning_titles(source.report)


def test_lineage_for_connector_skips_unrecognised_schema_strategy():
    source = _make_source()
    connector = _connector()
    config = {
        "configuration": [
            {
                "name": "Replication table schema",
                "properties": {
                    "Included Comma Separated Source Table Names": _wrap(
                        '"public"."testtable"'
                    )
                },
            },
            {
                "name": "Destination details",
                "properties": {
                    "Snowflake Destination Database": _wrap("OPENFLOW_DEV"),
                    "Destination Schema Strategy": _wrap("PREFIX"),
                },
            },
        ]
    }
    source._query_rows = _fake_get(json.dumps(config).encode())  # type: ignore[assignment]

    inlets, outlets = source._lineage_for_connector(connector)

    assert inlets == []
    assert outlets == []
    assert source.report.num_lineage_edges_skipped == 1
    assert "Unrecognised destination schema strategy" in _warning_titles(source.report)


def test_lineage_for_connector_counts_pattern_configured_connector():
    source = _make_source()
    connector = _connector()
    config = {
        "configuration": [
            {
                "name": "Replication table schema",
                "properties": {"Included Source Table Pattern": _wrap("public\\..*")},
            },
            {
                "name": "Destination details",
                "properties": {
                    "Snowflake Destination Database": _wrap("OPENFLOW_DEV"),
                    "Destination Schema Strategy": _wrap("SOURCE_SCHEMA"),
                },
            },
        ]
    }
    source._query_rows = _fake_get(json.dumps(config).encode())  # type: ignore[assignment]

    inlets, outlets = source._lineage_for_connector(connector)

    assert inlets == []
    assert outlets == []
    assert source.report.num_connectors_without_enumerable_tables == 1


def test_include_openflow_lineage_false_skips_lineage_entirely():
    # The gate lives in get_workunits_internal, not in _lineage_for_connector
    # itself, so this drives the full method rather than the helper directly.
    # _lineage_for_connector is stubbed to raise: if the gate were removed or
    # inverted, this test fails on that AssertionError rather than passing
    # vacuously.
    source = _make_source(include_openflow_lineage=False)
    connector = _connector()

    def fail_if_called(connector: OpenflowConnector) -> Any:
        raise AssertionError("_lineage_for_connector should not be called")

    source._lineage_for_connector = fail_if_called  # type: ignore[method-assign]

    connector_show = [
        {
            "name": connector.name,
            "runtime": connector.runtime_name,
            "connector_id": connector.connector_id,
            "connector_definition": connector.connector_definition,
        }
    ]

    def fake_query_rows(query: str) -> List[Dict[str, Any]]:
        if query == SnowflakeOpenflowQuery.show_deployments():
            return []
        if query == SnowflakeOpenflowQuery.show_runtimes():
            return []
        if query == SnowflakeOpenflowQuery.show_connectors():
            return connector_show
        if (
            DEPLOYMENT_HISTORY in query
            or RUNTIME_HISTORY in query
            or CONNECTOR_HISTORY in query
        ):
            return []
        raise AssertionError(f"unexpected query: {query!r}")

    source._query_rows = fake_query_rows  # type: ignore[method-assign]

    workunits = list(source.get_workunits_internal())  # must not raise

    assert workunits  # the flow and job workunits are still emitted


def test_lineage_inlet_uses_configured_source_platform_instance():
    # A Postgres recipe that sets platform_instance/env produces upstream URNs
    # carrying those coordinates. Without threading them through here, the inlet
    # is well-formed but names a dataset that recipe never emitted.
    source = _make_source(source_platform_instance="pg_prod", source_env="DEV")
    connector = _connector()
    source._query_rows = _fake_get(json.dumps(CONFIG_JSON).encode())  # type: ignore[assignment]

    inlets, outlets = source._lineage_for_connector(connector)

    assert inlets == [
        "urn:li:dataset:(urn:li:dataPlatform:postgres,pg_prod.appdb.public.testtable,DEV)"
    ]
    # The upstream coordinates must not leak into the destination side, which
    # keeps following snowflake_platform_instance / snowflake_env.
    assert outlets == [
        "urn:li:dataset:(urn:li:dataPlatform:snowflake,openflow_dev.public.testtable,PROD)"
    ]


def test_lineage_inlet_urn_unchanged_when_source_coordinates_unset():
    # Back-compatibility pin: with both new fields unset, the inlet URN must be
    # byte-identical to what shipped before they existed -- no platform_instance
    # segment, env from the source's own `env`. This is the assertion that
    # protects already-ingested lineage from being re-keyed.
    source = _make_source()
    connector = _connector()
    source._query_rows = _fake_get(json.dumps(CONFIG_JSON).encode())  # type: ignore[assignment]

    inlets, _ = source._lineage_for_connector(connector)

    assert inlets == [
        "urn:li:dataset:(urn:li:dataPlatform:postgres,appdb.public.testtable,PROD)"
    ]


def test_lineage_inlet_env_follows_openflow_env_when_source_env_unset():
    source = _make_source(env="DEV")
    connector = _connector()
    source._query_rows = _fake_get(json.dumps(CONFIG_JSON).encode())  # type: ignore[assignment]

    inlets, _ = source._lineage_for_connector(connector)

    assert inlets == [
        "urn:li:dataset:(urn:li:dataPlatform:postgres,appdb.public.testtable,DEV)"
    ]

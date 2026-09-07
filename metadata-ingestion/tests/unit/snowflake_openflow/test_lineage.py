import json
from typing import Any, Dict, List, Optional

import pytest

from datahub.ingestion.source.snowflake.snowflake_openflow import (
    SCHEMA_STRATEGY_SOURCE_SCHEMA,
    SnowflakeOpenflowSource,
    destination_identifier,
    parse_connector_config,
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

CONFIG_JSON = {
    "configuration": [
        {
            "name": "Source",
            "properties": {
                "JDBC URL": "jdbc:postgresql://host:5432/appdb",
                "Postgres Username": "repl",
            },
        },
        {
            "name": "Replication table schema",
            "properties": {
                "Included Comma Separated Source Table Names": '"public"."testtable"'
            },
        },
        {
            "name": "Destination details",
            "properties": {
                "Snowflake Destination Database": "OPENFLOW_DEV",
                "Destination Schema Strategy": "SOURCE_SCHEMA",
                "Object Identifier Resolution": "CASE_INSENSITIVE",
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
                "properties": {"Included Source Table Pattern": "public\\..*"},
            },
            {
                "name": "Destination details",
                "properties": {
                    "Snowflake Destination Database": "OPENFLOW_DEV",
                    "Destination Schema Strategy": "SOURCE_SCHEMA",
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
                    "Source Database Connection URL": "jdbc:postgresql://host:5432/appdb",
                    "Source Database User": "repl",
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
                "properties": {"Some Future Url Property": "jdbc:x://h/db"},
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
                    "Included Comma Separated Source Table Names": '"public"."a",noschema'
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
                "properties": {"Included Comma Separated Source Table Names": raw},
            }
        ]
    }
    assert parse_connector_config(config).source_tables == expected


# --- _lineage_for_connector / _read_connector_config: the orchestration ----
# --- layer that turns parsed config into report calls and URNs. Driven ----
# --- through the same _query_rows seam test_source.py uses -- no network. --

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


def _config_row(config_json: Dict[str, Any]) -> Dict[str, Any]:
    # SELECT $1 FROM '<uri>config.json' -- one row, one unnamed column.
    return {"$1": json.dumps(config_json)}


def _warning_titles(report: SnowflakeOpenflowReport) -> List[Optional[str]]:
    return [entry.title for entry in report.warnings]


def test_lineage_for_connector_happy_path_returns_inlets_and_outlets():
    source = _make_source()
    connector = _connector()
    source._query_rows = lambda query: [_config_row(CONFIG_JSON)]  # type: ignore[method-assign]

    inlets, outlets = source._lineage_for_connector(connector)

    assert outlets == [
        "urn:li:dataset:(urn:li:dataPlatform:snowflake,openflow_dev.public.testtable,PROD)"
    ]
    assert inlets == [
        "urn:li:dataset:(urn:li:dataPlatform:postgres,appdb.public.testtable,PROD)"
    ]
    assert source.report.num_lineage_edges == 1
    assert source.report.num_lineage_edges_skipped == 0


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
    # json.loads must stay inside the same try/except as the stage read --
    # otherwise invalid content crashes the whole ingestion run instead of
    # degrading to a per-connector warning.
    source = _make_source()
    connector = _connector()
    source._query_rows = lambda query: [{"$1": "not valid json"}]  # type: ignore[method-assign]

    inlets, outlets = source._lineage_for_connector(connector)

    assert inlets == []
    assert outlets == []
    assert source.report.num_config_reads_failed == 1
    assert "Could not read connector configuration" in _warning_titles(source.report)


def test_lineage_for_connector_reports_empty_row_as_config_read_failure():
    # A row with zero columns raises StopIteration out of next(iter(...)).
    # Nothing has confirmed the stage query returns exactly one row/column
    # against a live account -- this must degrade to the same warning as any
    # other malformed read, not crash the generator that drives ingestion.
    source = _make_source()
    connector = _connector()
    source._query_rows = lambda query: [{}]  # type: ignore[method-assign]

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
                    "Included Comma Separated Source Table Names": '"public"."testtable"'
                },
            },
            {
                "name": "Destination details",
                "properties": {
                    "Snowflake Destination Database": "OPENFLOW_DEV",
                    "Destination Schema Strategy": "PREFIX",
                },
            },
        ]
    }
    source._query_rows = lambda query: [_config_row(config)]  # type: ignore[method-assign]

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
                "properties": {"Included Source Table Pattern": "public\\..*"},
            },
            {
                "name": "Destination details",
                "properties": {
                    "Snowflake Destination Database": "OPENFLOW_DEV",
                    "Destination Schema Strategy": "SOURCE_SCHEMA",
                },
            },
        ]
    }
    source._query_rows = lambda query: [_config_row(config)]  # type: ignore[method-assign]

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

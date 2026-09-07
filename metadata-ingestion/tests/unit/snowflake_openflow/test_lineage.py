import pytest

from datahub.ingestion.source.snowflake.snowflake_openflow import (
    SCHEMA_STRATEGY_SOURCE_SCHEMA,
    destination_identifier,
    parse_connector_config,
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

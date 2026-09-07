from datahub.ingestion.source.snowflake.snowflake_openflow_query import (
    SnowflakeOpenflowQuery,
)

HISTORY_BUILDERS = [
    SnowflakeOpenflowQuery.deployment_history,
    SnowflakeOpenflowQuery.runtime_history,
    SnowflakeOpenflowQuery.connector_history,
]


def test_history_queries_target_account_usage():
    for builder in HISTORY_BUILDERS:
        assert "SNOWFLAKE.ACCOUNT_USAGE.OPENFLOW_" in builder(None)


def test_history_queries_never_use_offset():
    # OFFSET on a growing view silently skips and duplicates rows. Pagination is
    # a manual cursor on CREATED_ON.
    for builder in HISTORY_BUILDERS:
        assert "OFFSET" not in builder(None).upper()


def test_history_queries_paginate_by_created_on():
    for builder in HISTORY_BUILDERS:
        query = builder("2026-09-03T00:00:00")
        assert "CREATED_ON >" in query
        assert "ORDER BY CREATED_ON" in query


def test_first_page_has_no_cursor_predicate():
    assert "CREATED_ON >" not in SnowflakeOpenflowQuery.deployment_history(None)


def test_deleted_rows_are_retained_for_deletion_detection():
    # DELETED_ON must NOT be filtered in SQL: the deleted rows are what drives
    # stale-entity removal. Filtering happens in Python, after the merge.
    for builder in HISTORY_BUILDERS:
        assert "DELETED_ON IS NULL" not in builder(None).upper()


def test_show_commands_are_the_documented_grammar():
    assert SnowflakeOpenflowQuery.show_deployments() == "SHOW OPENFLOW DEPLOYMENTS"
    assert SnowflakeOpenflowQuery.show_runtimes() == "SHOW OPENFLOW RUNTIMES"
    assert SnowflakeOpenflowQuery.show_connectors() == "SHOW OPENFLOW CONNECTORS"


def test_stage_helpers_quote_the_uri_verbatim():
    # The version URI must be used exactly as the connector row reports it.
    # Hardcoding a path segment such as /versions/live/ produced Snowflake
    # errno 99112 "version live is not found".
    uri = "snow://openflow_connector/OPENFLOW_DEV.OPENFLOW_OBJECTS.pg/versions/3/"
    assert SnowflakeOpenflowQuery.list_stage(uri) == f"LIST '{uri}'"
    query = SnowflakeOpenflowQuery.get_stage_file_to_local(uri, "config.json", "/tmp/x")
    assert uri in query
    assert query.startswith("GET ")
    assert "'file:///tmp/x'" in query


def test_page_size_attribute_is_accessible():
    # Task 8 reads SnowflakeOpenflowQuery.PAGE_SIZE to decide when a page is last.
    # Verify it exists and matches the LIMIT in generated queries.
    assert hasattr(SnowflakeOpenflowQuery, "PAGE_SIZE")
    assert SnowflakeOpenflowQuery.PAGE_SIZE == 1000
    assert (
        f"LIMIT {SnowflakeOpenflowQuery.PAGE_SIZE}"
        in SnowflakeOpenflowQuery.deployment_history(None)
    )

from datahub.ingestion.source.snowflake.snowflake_openflow_models import (
    OpenflowConnector,
    OpenflowDeployment,
    OpenflowRuntime,
    merge_show_and_history,
)


def test_deployment_from_show_row():
    deployment = OpenflowDeployment.from_row(
        {"name": "IngestionTest", "key": "hq8crgi3", "status": "ACTIVE", "owner": "R"}
    )
    assert deployment is not None
    assert deployment.key == "hq8crgi3"
    assert deployment.name == "IngestionTest"
    assert deployment.owner == "R"


def test_deployment_from_history_row_uses_uppercase_column_names():
    # The ACCOUNT_USAGE views return uppercase keys; SHOW returns lowercase.
    # One dataclass must read both without the caller normalising first.
    deployment = OpenflowDeployment.from_row(
        {"NAME": "IngestionTest", "DEPLOYMENT_KEY": "hq8crgi3", "DELETED_ON": None}
    )
    assert deployment is not None
    assert deployment.key == "hq8crgi3"
    assert deployment.name == "IngestionTest"


def test_absent_optional_columns_do_not_raise():
    # The views are still evolving; a dropped optional column must degrade, not crash.
    deployment = OpenflowDeployment.from_row({"name": "D", "key": "k"})
    assert deployment is not None
    assert deployment.status is None
    assert deployment.created_on is None


def test_row_without_identity_key_returns_none():
    assert OpenflowDeployment.from_row({"status": "ACTIVE"}) is None


def test_runtime_carries_parent_deployment():
    runtime = OpenflowRuntime.from_row(
        {
            "name": "IngestionTest",
            "key": "ingestiontest-100",
            "deployment": "IngestionTest",
            "database_name": "OPENFLOW_DEV",
            "schema_name": "OPENFLOW_OBJECTS",
        }
    )
    assert runtime is not None
    assert runtime.key == "ingestiontest-100"
    assert runtime.deployment_name == "IngestionTest"
    # database_name/schema_name are the runtime OBJECT's own location, never a
    # data destination. Kept for the object's properties only.
    assert runtime.object_database == "OPENFLOW_DEV"


def test_connector_identity_is_the_connector_id():
    connector = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 1,
            "NAME": "pg_cdc",
            "RUNTIME_NAME": "IngestionTest",
            "CONNECTOR_DEFINITION": "OPENFLOW_POSTGRES_CDC",
        }
    )
    assert connector is not None
    assert connector.connector_id == "1"
    assert connector.connector_definition == "OPENFLOW_POSTGRES_CDC"


def test_merge_prefers_show_for_location_and_history_for_ids():
    show = OpenflowRuntime.from_row(
        {
            "name": "R",
            "key": "r-100",
            "deployment": "D",
            "database_name": "OPENFLOW_DEV",
        }
    )
    history = OpenflowRuntime.from_row(
        {
            "NAME": "R",
            "RUNTIME_KEY": "r-100",
            "DATABASE_NAME": None,
            "EXECUTE_AS_ROLE_NAME": "RUNTIME_ROLE",
            "CREATED_ON": "2026-09-03T00:00:00",
        }
    )
    assert show is not None
    assert history is not None
    merged = merge_show_and_history([show], [history])
    assert len(merged) == 1
    # SHOW wins for location: the view returned NULL where SHOW was populated.
    assert merged[0].object_database == "OPENFLOW_DEV"
    # The view wins for what only it carries.
    assert merged[0].execute_as_role == "RUNTIME_ROLE"
    assert merged[0].created_on == "2026-09-03T00:00:00"


def test_merge_treats_show_only_object_as_new_not_deleted():
    # Measured: a runtime visible to SHOW was absent from the view ~20 min after
    # creation. Treating that as a deletion would drop a brand-new object.
    show = OpenflowRuntime.from_row({"name": "R", "key": "r-100", "deployment": "D"})
    assert show is not None
    merged = merge_show_and_history([show], [])
    assert len(merged) == 1
    assert merged[0].key == "r-100"

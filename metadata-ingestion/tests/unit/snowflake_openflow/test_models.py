from datahub.ingestion.source.snowflake.snowflake_openflow_models import (
    OpenflowConnector,
    OpenflowDeployment,
    OpenflowRuntime,
    merge_show_and_history,
)


def test_deployment_from_show_row():
    deployment = OpenflowDeployment.from_row(
        {"name": "MyDeployment", "key": "abc12345", "status": "ACTIVE", "owner": "R"}
    )
    assert deployment is not None
    assert deployment.key == "abc12345"
    assert deployment.name == "MyDeployment"
    assert deployment.owner == "R"


def test_deployment_from_history_row_uses_uppercase_column_names():
    # The ACCOUNT_USAGE views return uppercase keys; SHOW returns lowercase.
    # One dataclass must read both without the caller normalising first.
    deployment = OpenflowDeployment.from_row(
        {"NAME": "MyDeployment", "DEPLOYMENT_KEY": "abc12345", "DELETED_ON": None}
    )
    assert deployment is not None
    assert deployment.key == "abc12345"
    assert deployment.name == "MyDeployment"


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
            "name": "MyRuntime",
            "key": "myruntime-1",
            "deployment": "MyDeployment",
            "database_name": "MY_DB",
            "schema_name": "MY_SCHEMA",
        }
    )
    assert runtime is not None
    assert runtime.key == "myruntime-1"
    assert runtime.deployment_name == "MyDeployment"
    # database_name/schema_name are the runtime OBJECT's own location, never a
    # data destination. Kept for the object's properties only.
    assert runtime.object_database == "MY_DB"


def test_connector_from_row_populates_connector_id_field():
    # CONNECTOR_ID is carried as an ordinary field when the view supplies it,
    # but is no longer the identity — see test_connector_key_* below.
    connector = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 1,
            "NAME": "pg_cdc",
            "RUNTIME_NAME": "MyRuntime",
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
            "database_name": "MY_DB",
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
    assert merged[0].object_database == "MY_DB"
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


def test_merge_does_not_mutate_caller_owned_show_row():
    # show_rows is caller-owned; a downstream caller keeps its own reference to
    # the objects it passes in, so the merge must not setattr onto them.
    show = OpenflowRuntime.from_row(
        {
            "name": "R",
            "key": "r-100",
            "deployment": "D",
            "database_name": "MY_DB",
        }
    )
    history = OpenflowRuntime.from_row(
        {"NAME": "R", "RUNTIME_KEY": "r-100", "EXECUTE_AS_ROLE_NAME": "RUNTIME_ROLE"}
    )
    assert show is not None
    assert history is not None
    merge_show_and_history([show], [history])
    # The original object passed in show_rows must be untouched by the merge.
    assert show.execute_as_role is None
    assert show.object_database == "MY_DB"


def test_connector_key_disambiguates_same_name_across_runtimes():
    # SHOW OPENFLOW CONNECTORS is account-wide, so a merge is genuinely called
    # with connectors of the same name under different runtimes; they must not
    # collide into a single identity.
    first = OpenflowConnector.from_row({"name": "pg_cdc", "runtime": "MyRuntime"})
    second = OpenflowConnector.from_row({"name": "pg_cdc", "runtime": "OtherRuntime"})
    assert first is not None
    assert second is not None
    assert first.key != second.key


def test_connector_key_stable_without_connector_id():
    # A newly created connector has no CONNECTOR_ID yet (the view lags ~20
    # minutes behind SHOW), so identity must not depend on it.
    connector = OpenflowConnector.from_row({"name": "pg_cdc", "runtime": "MyRuntime"})
    assert connector is not None
    assert connector.connector_id is None
    assert connector.key == "MyRuntime/pg_cdc"


def test_recreated_object_is_not_reported_as_deleted():
    # These views are append-style lifecycle records and `key` is not
    # per-incarnation: a connector's key is the composite <runtime>/<name>, stable
    # across a drop and re-create under the same name. So one key can own several
    # rows, the older ones carrying DELETED_ON.
    #
    # Without newest-wins resolution the superseded incarnation's DELETED_ON merges
    # onto the live object (the field merge fills any None field, and deleted_on on
    # a live row IS None), the caller's `deleted_on is None` filter then drops an
    # object that exists, and stale-entity removal soft-deletes it in DataHub. No
    # counter reflects that, which is why it needs a test rather than a comment.
    show = OpenflowConnector.from_row({"name": "pg_cdc", "runtime": "MyRuntime"})
    deleted_incarnation = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 1,
            "NAME": "pg_cdc",
            "RUNTIME_NAME": "MyRuntime",
            "CREATED_ON": "2026-01-01T00:00:00",
            "DELETED_ON": "2026-02-01T00:00:00",
        }
    )
    live_incarnation = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 2,
            "NAME": "pg_cdc",
            "RUNTIME_NAME": "MyRuntime",
            "CREATED_ON": "2026-03-01T00:00:00",
        }
    )
    assert show is not None and deleted_incarnation is not None
    assert live_incarnation is not None

    # Both iteration orders, because the defect was order-independent.
    for history in (
        [deleted_incarnation, live_incarnation],
        [live_incarnation, deleted_incarnation],
    ):
        merged = merge_show_and_history([show], history)
        assert len(merged) == 1
        assert merged[0].deleted_on is None, (
            "a re-created object must not inherit the DELETED_ON of the "
            "incarnation it replaced"
        )
        # The newest incarnation's surrogate id wins too, not the dead one's.
        assert merged[0].connector_id == "2"


def test_object_deleted_and_not_recreated_is_still_reported_deleted():
    # The mirror case: newest-wins must not make deletion undetectable, or the
    # deletion-detection feature the unfiltered DELETED_ON exists for is lost.
    deleted = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 1,
            "NAME": "gone_cdc",
            "RUNTIME_NAME": "MyRuntime",
            "CREATED_ON": "2026-01-01T00:00:00",
            "DELETED_ON": "2026-02-01T00:00:00",
        }
    )
    assert deleted is not None
    merged = merge_show_and_history([], [deleted])
    assert len(merged) == 1
    assert merged[0].deleted_on == "2026-02-01T00:00:00"


def test_show_present_object_is_never_marked_deleted_by_a_history_row():
    # SHOW is authoritative for existence. This is the invariant that makes the
    # whole "live object soft-deleted by a stale lifecycle row" class unreachable,
    # independently of whether CREATED_ON is populated at all -- which is the case
    # the timestamp-ordering approach got wrong, because the view populates
    # CREATED_ON with a lag (the pager has a guard for NULL CREATED_ON precisely
    # because it happens).
    show = OpenflowConnector.from_row({"name": "pg_cdc", "runtime": "MyRuntime"})
    deleted_untimestamped = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 1,
            "NAME": "pg_cdc",
            "RUNTIME_NAME": "MyRuntime",
            "DELETED_ON": "2026-02-01T00:00:00",
        }
    )
    assert show is not None and deleted_untimestamped is not None
    merged = merge_show_and_history([show], [deleted_untimestamped])
    assert len(merged) == 1
    assert merged[0].deleted_on is None
    # The other fields still merge from the view.
    assert merged[0].connector_id == "1"


def test_show_present_object_survives_a_created_on_tie():
    # The reviewer reproduced a tie resolving by iteration order. With SHOW
    # authoritative for existence, order cannot matter for liveness.
    show = OpenflowConnector.from_row({"name": "pg_cdc", "runtime": "MyRuntime"})
    same_ts_open = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 2,
            "NAME": "pg_cdc",
            "RUNTIME_NAME": "MyRuntime",
            "CREATED_ON": "2026-03-01T00:00:00",
        }
    )
    same_ts_closed = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 1,
            "NAME": "pg_cdc",
            "RUNTIME_NAME": "MyRuntime",
            "CREATED_ON": "2026-03-01T00:00:00",
            "DELETED_ON": "2026-03-02T00:00:00",
        }
    )
    assert show is not None and same_ts_open is not None and same_ts_closed is not None
    for history in ([same_ts_open, same_ts_closed], [same_ts_closed, same_ts_open]):
        merged = merge_show_and_history([show], history)
        assert len(merged) == 1
        assert merged[0].deleted_on is None


def test_view_only_deleted_object_still_reports_deleted_on_a_tie():
    # The mirror risk: SHOW authority must not make deletion undetectable for an
    # object SHOW no longer lists. An open and a closed row of the same key with
    # the SAME timestamp must resolve to open only when SHOW vouches for it; with
    # no SHOW row, a closed-only history must stay closed.
    closed = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 1,
            "NAME": "gone_cdc",
            "RUNTIME_NAME": "MyRuntime",
            "CREATED_ON": "2026-03-01T00:00:00",
            "DELETED_ON": "2026-03-02T00:00:00",
        }
    )
    assert closed is not None
    merged = merge_show_and_history([], [closed])
    assert len(merged) == 1
    assert merged[0].deleted_on == "2026-03-02T00:00:00"


def test_view_only_key_prefers_the_open_incarnation():
    # Exercises the open-beats-closed branch of the per-key resolver, which only
    # governs keys SHOW did not list -- for SHOW-present keys the authority rule
    # above already decides liveness, which is why that rule alone left this
    # branch unexercised.
    #
    # READ THE ASSUMPTION BEFORE TRUSTING THIS TEST. The rule, and therefore this
    # data, assumes these are INCARNATION-style views: one row per object life,
    # so an open row and a closed row under one key are two different lives and
    # the open one is the object that exists. Under that reading the data below is
    # a connector invisible to SHOW for privilege reasons whose current life is
    # open, alongside a previous life that ended.
    #
    # It is NOT valid under an EVENT-style reading, where rows are lifecycle
    # events for one object and a create row stays open forever -- there the
    # latest event governs, this assertion is wrong, and DELETION_DETECTION would
    # never fire for a view-only key. Note the shape that distinguishes
    # open-beats-closed from plain newest-first REQUIRES the open row to be the
    # older one, which is itself hard to realise under the incarnation reading.
    #
    # Which reading is right is unverified: all three history views hold exactly
    # one row in the account available here, so there is no churn to observe. One
    # `GROUP BY <surrogate id> HAVING COUNT(*) > 1` against an account with churn
    # settles it. Snowflake's standard object-catalog shape (CREATED_ON +
    # LAST_ALTERED_ON + DELETED_ON + surrogate id) points to incarnation-style,
    # which is why the rule is written this way -- but it is an assumption, not a
    # measurement, and the SHOW-authority rule above is the one that holds either
    # way.
    closed_newer = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 1,
            "NAME": "priv_cdc",
            "RUNTIME_NAME": "MyRuntime",
            "CREATED_ON": "2026-05-01T00:00:00",
            "DELETED_ON": "2026-05-02T00:00:00",
        }
    )
    open_older = OpenflowConnector.from_row(
        {
            "CONNECTOR_ID": 2,
            "NAME": "priv_cdc",
            "RUNTIME_NAME": "MyRuntime",
            "CREATED_ON": "2026-04-01T00:00:00",
        }
    )
    assert closed_newer is not None and open_older is not None
    # Open wins even though the closed row carries the NEWER timestamp, and in
    # both iteration orders.
    for history in ([closed_newer, open_older], [open_older, closed_newer]):
        merged = merge_show_and_history([], history)
        assert len(merged) == 1
        assert merged[0].deleted_on is None
        assert merged[0].connector_id == "2"

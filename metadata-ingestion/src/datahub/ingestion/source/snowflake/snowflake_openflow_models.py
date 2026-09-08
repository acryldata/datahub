import dataclasses
from typing import Any, Dict, List, Optional, TypeVar

COL_NAME = "NAME"
COL_DEPLOYMENT_KEY = "DEPLOYMENT_KEY"
COL_RUNTIME_KEY = "RUNTIME_KEY"
COL_CONNECTOR_ID = "CONNECTOR_ID"
COL_KEY = "KEY"
COL_STATUS = "STATUS"
COL_OWNER = "OWNER"
COL_CREATED_ON = "CREATED_ON"
COL_DELETED_ON = "DELETED_ON"
COL_DEPLOYMENT = "DEPLOYMENT"
COL_DEPLOYMENT_NAME = "DEPLOYMENT_NAME"
COL_RUNTIME = "RUNTIME"
COL_RUNTIME_NAME = "RUNTIME_NAME"
COL_DATABASE_NAME = "DATABASE_NAME"
COL_SCHEMA_NAME = "SCHEMA_NAME"
COL_EXECUTE_AS_ROLE = "EXECUTE_AS_ROLE"
COL_EXECUTE_AS_ROLE_NAME = "EXECUTE_AS_ROLE_NAME"
COL_CONNECTOR_DEFINITION = "CONNECTOR_DEFINITION"
COL_DEFAULT_VERSION = "DEFAULT_VERSION"
COL_DEFAULT_VERSION_LOCATION_URI = "DEFAULT_VERSION_LOCATION_URI"
COL_DISPLAY_NAME = "DISPLAY_NAME"


def get_col(row: Dict[str, Any], *names: str) -> Optional[Any]:
    # SHOW returns lowercase keys, the ACCOUNT_USAGE views uppercase, and the
    # views are still evolving, so read case-insensitively by name and tolerate
    # absence. Never read positionally.
    folded = {str(key).upper(): value for key, value in row.items()}
    for name in names:
        value = folded.get(name.upper())
        if value is not None:
            return value
    return None


def get_str(row: Dict[str, Any], *names: str) -> Optional[str]:
    value = get_col(row, *names)
    return None if value is None else str(value)


@dataclasses.dataclass
class OpenflowDeployment:
    key: str
    name: Optional[str] = None
    status: Optional[str] = None
    owner: Optional[str] = None
    display_name: Optional[str] = None
    created_on: Optional[str] = None
    deleted_on: Optional[str] = None

    @classmethod
    def from_row(cls, row: Dict[str, Any]) -> Optional["OpenflowDeployment"]:
        key = get_str(row, COL_DEPLOYMENT_KEY, COL_KEY)
        if key is None:
            return None
        return cls(
            key=key,
            name=get_str(row, COL_NAME),
            status=get_str(row, COL_STATUS),
            owner=get_str(row, COL_OWNER),
            display_name=get_str(row, COL_DISPLAY_NAME),
            created_on=get_str(row, COL_CREATED_ON),
            deleted_on=get_str(row, COL_DELETED_ON),
        )


@dataclasses.dataclass
class OpenflowRuntime:
    key: str
    name: Optional[str] = None
    deployment_name: Optional[str] = None
    status: Optional[str] = None
    owner: Optional[str] = None
    display_name: Optional[str] = None
    object_database: Optional[str] = None
    object_schema: Optional[str] = None
    execute_as_role: Optional[str] = None
    created_on: Optional[str] = None
    deleted_on: Optional[str] = None

    @classmethod
    def from_row(cls, row: Dict[str, Any]) -> Optional["OpenflowRuntime"]:
        key = get_str(row, COL_RUNTIME_KEY, COL_KEY)
        if key is None:
            return None
        return cls(
            key=key,
            name=get_str(row, COL_NAME),
            deployment_name=get_str(row, COL_DEPLOYMENT, COL_DEPLOYMENT_NAME),
            status=get_str(row, COL_STATUS),
            owner=get_str(row, COL_OWNER),
            display_name=get_str(row, COL_DISPLAY_NAME),
            object_database=get_str(row, COL_DATABASE_NAME),
            object_schema=get_str(row, COL_SCHEMA_NAME),
            execute_as_role=get_str(row, COL_EXECUTE_AS_ROLE, COL_EXECUTE_AS_ROLE_NAME),
            created_on=get_str(row, COL_CREATED_ON),
            deleted_on=get_str(row, COL_DELETED_ON),
        )


@dataclasses.dataclass
class OpenflowConnector:
    # Neither surface has a usable single-column id: CONNECTOR_HISTORY has no
    # key column at all (CONNECTOR_ID is the only stable id it carries, and it
    # lags ~20 minutes behind SHOW), while SHOW OPENFLOW CONNECTORS carries no
    # id column at all. Connector names are also not unique account-wide (SHOW
    # OPENFLOW CONNECTORS is account-wide, spanning many runtimes), so identity
    # must be the composite of (runtime, name) that both surfaces agree on.
    name: str
    runtime_name: str
    connector_id: Optional[str] = None
    connector_definition: Optional[str] = None
    status: Optional[str] = None
    owner: Optional[str] = None
    display_name: Optional[str] = None
    default_version: Optional[str] = None
    version_location_uri: Optional[str] = None
    created_on: Optional[str] = None
    deleted_on: Optional[str] = None

    @classmethod
    def from_row(cls, row: Dict[str, Any]) -> Optional["OpenflowConnector"]:
        name = get_str(row, COL_NAME)
        runtime_name = get_str(row, COL_RUNTIME, COL_RUNTIME_NAME)
        # Both halves of the composite identity are required. Both surfaces
        # carry the runtime association (SHOW as `runtime`, the view as
        # RUNTIME_NAME), so a row missing it is not a usable connector rather
        # than one with a degraded key.
        if name is None or runtime_name is None:
            return None
        return cls(
            name=name,
            runtime_name=runtime_name,
            connector_id=get_str(row, COL_CONNECTOR_ID),
            connector_definition=get_str(row, COL_CONNECTOR_DEFINITION),
            status=get_str(row, COL_STATUS),
            owner=get_str(row, COL_OWNER),
            display_name=get_str(row, COL_DISPLAY_NAME),
            default_version=get_str(row, COL_DEFAULT_VERSION),
            version_location_uri=get_str(row, COL_DEFAULT_VERSION_LOCATION_URI),
            created_on=get_str(row, COL_CREATED_ON),
            deleted_on=get_str(row, COL_DELETED_ON),
        )

    @property
    def key(self) -> str:
        # "/" is not among the URN reserved characters (see
        # datahub.utilities.urn_encoder.RESERVED_CHARS), so it needs no
        # escaping when this key ends up inside a DataFlow URN component.
        return f"{self.runtime_name}/{self.name}"


RowModel = TypeVar("RowModel", OpenflowDeployment, OpenflowRuntime, OpenflowConnector)


def _resolve_per_key(rows: List[RowModel]) -> List[RowModel]:
    # OPEN beats CLOSED first, and only then newest-first.
    #
    # An earlier revision ordered on CREATED_ON alone with absent timestamps
    # sorting oldest. That re-opened the very defect this resolver exists to close:
    # the view populates CREATED_ON with a lag (Guard 1 in the pager exists because
    # a NULL CREATED_ON really occurs), so a freshly re-created incarnation can
    # arrive without one. Ordering on the timestamp handed the key back to the older
    # DELETED_ON row exactly then, and the caller's `deleted_on is None` filter drops
    # the live object and stale-entity removal soft-deletes it.
    #
    # An object is live if ANY of its lifecycle rows is still open, so an open row
    # wins regardless of timestamp. Among rows in the same state, newest CREATED_ON
    # wins, which keeps the most recent deletion for an object that really is gone.
    def is_open(row: RowModel) -> bool:
        return row.deleted_on is None

    resolved: Dict[str, RowModel] = {}
    for row in rows:
        current = resolved.get(row.key)
        if current is None:
            resolved[row.key] = row
            continue
        if is_open(row) != is_open(current):
            if is_open(row):
                resolved[row.key] = row
            continue
        # Same state: newest wins. Absent timestamps compare as "" and so lose to
        # any real one, which is harmless here because both rows agree on liveness.
        if (row.created_on or "") >= (current.created_on or ""):
            resolved[row.key] = row
    return list(resolved.values())


def merge_show_and_history(
    show_rows: List[RowModel], history_rows: List[RowModel]
) -> List[RowModel]:
    # SHOW is authoritative for object location (the views returned NULL
    # DATABASE_NAME where SHOW was populated). The views are authoritative for
    # what only they carry: surrogate ids, timestamps, EXECUTE_AS_ROLE_NAME.
    #
    # Present in SHOW but absent from the view means NEW, never deleted: a
    # runtime visible to SHOW was still missing from the view ~20 minutes after
    # creation, so a view-only reading would report zero runtimes to a user who
    # had just created one.
    # Non-mutating by design: `show_rows` items are caller-owned, and at least
    # one downstream caller keeps its own reference to them after calling this
    # function. dataclasses.replace() builds a new merged instance instead of
    # setattr-ing onto the caller's object, so show_rows and its elements are
    # left untouched.
    # Resolve the history side per key BEFORE merging. These views are append-style
    # lifecycle records, and `key` is not per-incarnation: a connector's key is the
    # composite <runtime_name>/<name> (see OpenflowConnector.key), which is stable
    # across a drop and re-create under the same name, and each incarnation carries
    # its own CONNECTOR_ID / RUNTIME_ID / DEPLOYMENT_ID surrogate. So one key can
    # legitimately own several rows, of which the older ones carry DELETED_ON.
    #
    # Merging them naively lets a superseded incarnation's DELETED_ON survive onto
    # the live object -- the field-level merge below fills any None field from the
    # history row, and `deleted_on` on a live row IS None -- after which the
    # caller's `deleted_on is None` filter drops an object that exists and stale
    # entity removal soft-deletes it. Newest CREATED_ON wins, so an object counts as
    # deleted only when its most recent lifecycle row says so.
    history_rows = _resolve_per_key(history_rows)

    by_key: Dict[str, RowModel] = {row.key: row for row in show_rows}
    for history_row in history_rows:
        existing = by_key.get(history_row.key)
        if existing is None:
            # View-only: either genuinely deleted, or invisible to SHOW for
            # privilege reasons. Deleted rows are filtered by the caller.
            by_key[history_row.key] = history_row
            continue
        # `deleted_on` is EXCLUDED for a SHOW-present key. SHOW is authoritative
        # for existence -- it lists what is there now -- so no lifecycle row may
        # mark a SHOW-visible object deleted. This is what makes the whole class
        # "a live object soft-deleted because some history row said deleted"
        # unreachable, rather than merely unlikely: it holds regardless of whether
        # CREATED_ON is populated, whether these views are incarnation-style or
        # event-style, how a timestamp tie resolves, and whether lexicographic
        # order matches real time across a DST fall-back. The per-key resolution
        # above still decides which lifecycle row supplies the OTHER fields, and
        # still governs view-only keys, where SHOW has said nothing.
        updates = {
            field.name: getattr(history_row, field.name)
            for field in dataclasses.fields(existing)
            if field.name != "deleted_on"
            and getattr(existing, field.name) is None
            and getattr(history_row, field.name) is not None
        }
        if updates:
            by_key[history_row.key] = dataclasses.replace(existing, **updates)
    return list(by_key.values())

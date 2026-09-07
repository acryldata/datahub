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
    connector_id: str
    name: Optional[str] = None
    runtime_name: Optional[str] = None
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
        # CONNECTOR_HISTORY has no key column; CONNECTOR_ID is the only stable id.
        # SHOW OPENFLOW CONNECTORS carries no id at all, so a SHOW-only row falls
        # back to its name and is reconciled against the view by name.
        connector_id = get_str(row, COL_CONNECTOR_ID) or get_str(row, COL_NAME)
        if connector_id is None:
            return None
        return cls(
            connector_id=connector_id,
            name=get_str(row, COL_NAME),
            runtime_name=get_str(row, COL_RUNTIME, COL_RUNTIME_NAME),
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
        return self.connector_id


RowModel = TypeVar("RowModel", OpenflowDeployment, OpenflowRuntime, OpenflowConnector)


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
    by_key: Dict[str, RowModel] = {row.key: row for row in show_rows}
    for history_row in history_rows:
        existing = by_key.get(history_row.key)
        if existing is None:
            # View-only: either genuinely deleted, or invisible to SHOW for
            # privilege reasons. Deleted rows are filtered by the caller.
            by_key[history_row.key] = history_row
            continue
        for field in dataclasses.fields(existing):
            if getattr(existing, field.name) is None:
                setattr(existing, field.name, getattr(history_row, field.name))
    return list(by_key.values())

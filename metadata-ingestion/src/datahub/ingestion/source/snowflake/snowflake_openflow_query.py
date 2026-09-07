from typing import Optional

ACCOUNT_USAGE = "SNOWFLAKE.ACCOUNT_USAGE"
DEPLOYMENT_HISTORY = f"{ACCOUNT_USAGE}.OPENFLOW_DEPLOYMENT_HISTORY"
RUNTIME_HISTORY = f"{ACCOUNT_USAGE}.OPENFLOW_RUNTIME_HISTORY"
CONNECTOR_HISTORY = f"{ACCOUNT_USAGE}.OPENFLOW_CONNECTOR_HISTORY"
PAGE_SIZE = 1000


def _history_query(view: str, cursor: Optional[str]) -> str:
    # Manual cursor on CREATED_ON. OFFSET is never used: these views grow while
    # being read, and OFFSET would skip and duplicate rows across pages.
    #
    # DELETED_ON is deliberately NOT filtered here. The deleted rows are the
    # input to deletion detection; filtering them in SQL would throw away the
    # only observable signal that an object is gone.
    predicate = f"WHERE CREATED_ON > '{cursor}'" if cursor else ""
    return f"""
SELECT *
FROM {view}
{predicate}
ORDER BY CREATED_ON
LIMIT {PAGE_SIZE}
""".strip()


class SnowflakeOpenflowQuery:
    PAGE_SIZE = PAGE_SIZE

    @staticmethod
    def show_deployments() -> str:
        return "SHOW OPENFLOW DEPLOYMENTS"

    @staticmethod
    def show_runtimes() -> str:
        return "SHOW OPENFLOW RUNTIMES"

    @staticmethod
    def show_connectors() -> str:
        return "SHOW OPENFLOW CONNECTORS"

    @staticmethod
    def deployment_history(cursor: Optional[str]) -> str:
        return _history_query(DEPLOYMENT_HISTORY, cursor)

    @staticmethod
    def runtime_history(cursor: Optional[str]) -> str:
        return _history_query(RUNTIME_HISTORY, cursor)

    @staticmethod
    def connector_history(cursor: Optional[str]) -> str:
        return _history_query(CONNECTOR_HISTORY, cursor)

    @staticmethod
    def event_table_parameter() -> str:
        return "SHOW PARAMETERS LIKE 'EVENT_TABLE' IN ACCOUNT"

    @staticmethod
    def list_stage(version_location_uri: str) -> str:
        return f"LIST '{version_location_uri}'"

    @staticmethod
    def get_stage_file(version_location_uri: str, filename: str) -> str:
        # The URI is used verbatim as reported by the connector row. Substituting
        # a guessed version segment fails with errno 99112.
        return f"SELECT $1 FROM '{version_location_uri}{filename}'"

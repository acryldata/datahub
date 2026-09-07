from typing import Optional

from pydantic import Field, model_validator

from datahub.configuration.common import AllowDenyPattern
from datahub.configuration.source_common import DatasetSourceConfigMixin
from datahub.ingestion.source.snowflake.snowflake_config import (
    SnowflakeIdentifierConfig,
)
from datahub.ingestion.source.snowflake.snowflake_connection import (
    SnowflakeConnectionConfig,
)
from datahub.ingestion.source.state.stateful_ingestion_base import (
    StatefulIngestionConfigBase,
)


class SnowflakeOpenflowSourceConfig(
    StatefulIngestionConfigBase, DatasetSourceConfigMixin
):
    connection: SnowflakeConnectionConfig = Field(
        description="Snowflake connection details. Reused unchanged from the snowflake "
        "family, so key-pair and OAuth authentication behave identically.",
    )

    deployment_pattern: AllowDenyPattern = Field(
        default=AllowDenyPattern.allow_all(),
        description="Regex patterns for Openflow deployments to filter in ingestion.",
    )
    runtime_pattern: AllowDenyPattern = Field(
        default=AllowDenyPattern.allow_all(),
        description="Regex patterns for Openflow runtimes to filter in ingestion.",
    )
    connector_pattern: AllowDenyPattern = Field(
        default=AllowDenyPattern.allow_all(),
        description="Regex patterns for Openflow connectors to filter in ingestion.",
    )

    snowflake_platform_instance: Optional[str] = Field(
        default=None,
        description="The `platform_instance` of the Snowflake ingestion that owns the "
        "tables Openflow writes to. Must match that recipe exactly. A mismatch "
        "produces well-formed lineage pointing at datasets that do not exist, which "
        "fails silently.",
    )
    snowflake_env: Optional[str] = Field(
        default=None,
        description="The `env` of the Snowflake ingestion that owns the destination "
        "tables. Defaults to this source's own `env`.",
    )
    convert_urns_to_lowercase: bool = Field(
        default=True,
        description="Whether to lowercase the destination Snowflake dataset URNs. Must "
        "match the `snowflake` recipe pointed at the same account, or the URNs will "
        "not line up.",
    )

    include_openflow_lineage: bool = Field(
        default=True,
        description="Emit table-level lineage from each connector's configuration to "
        "the Snowflake tables it writes.",
    )
    include_run_history: bool = Field(
        default=False,
        description="Emit connector run history as DataProcessInstances. Requires read "
        "access to the account's event table, which is a grant beyond the standard "
        "ACCOUNT_USAGE set, so this defaults off and is feature-detected at runtime.",
    )
    event_table: Optional[str] = Field(
        default=None,
        description="Fully qualified event table holding Openflow telemetry. "
        "Auto-discovered from the account's EVENT_TABLE parameter when unset.",
    )

    @model_validator(mode="after")
    def default_snowflake_env_to_env(self) -> "SnowflakeOpenflowSourceConfig":
        if self.snowflake_env is None:
            self.snowflake_env = self.env
        return self

    def get_snowflake_identifier_config(self) -> SnowflakeIdentifierConfig:
        return SnowflakeIdentifierConfig(
            platform_instance=self.snowflake_platform_instance,
            env=self.snowflake_env,
            convert_urns_to_lowercase=self.convert_urns_to_lowercase,
        )

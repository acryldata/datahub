import pytest

from datahub.ingestion.source.source_registry import source_registry


def test_source_is_registered_under_its_recipe_type():
    source_class = source_registry.get("snowflake-openflow")
    assert source_class.__name__ == "SnowflakeOpenflowSource"


# The @config_class decorator (which adds get_config_class()) is attached to
# SnowflakeOpenflowSource by Task 11, not this task. Until that lands, this is
# expected to fail; strict=True turns an unexpected pass into a failure so the
# marker gets removed the moment Task 11 makes it real.
@pytest.mark.xfail(
    reason="@config_class decorator is added to SnowflakeOpenflowSource by Task 11",
    strict=True,
)
def test_registered_class_exposes_its_config():
    from datahub.ingestion.api.decorators import config_class  # noqa: F401

    source_class = source_registry.get("snowflake-openflow")
    assert (
        source_class.get_config_class().__name__  # type: ignore[attr-defined]
        == "SnowflakeOpenflowSourceConfig"
    )

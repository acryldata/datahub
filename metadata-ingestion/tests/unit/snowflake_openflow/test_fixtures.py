import pathlib

import pytest
import yaml

from datahub.ingestion.source.snowflake.snowflake_openflow_config import (
    SnowflakeOpenflowSourceConfig,
)

FIXTURE_DIR = pathlib.Path("tests/integration/snowflake_openflow/fixtures")
FIXTURES = ["inventory.yml", "lineage.yml", "capabilities.yml"]


@pytest.mark.parametrize("filename", FIXTURES)
def test_fixture_recipe_parses(filename):
    recipe = yaml.safe_load((FIXTURE_DIR / filename).read_text())
    assert recipe["source"]["type"] == "snowflake-openflow"


@pytest.mark.parametrize("filename", FIXTURES)
def test_every_recipe_key_exists_on_the_config(filename):
    # ConfigModel is extra="forbid", so an unknown key fails at load time rather
    # than being ignored. Placeholder ${VARS} are left unresolved here, so only
    # the key names are checked, not their values.
    recipe = yaml.safe_load((FIXTURE_DIR / filename).read_text())
    declared = set(SnowflakeOpenflowSourceConfig.model_fields.keys())
    used = set(recipe["source"]["config"].keys())
    assert used <= declared, f"unknown config keys: {sorted(used - declared)}"

from datahub.ingestion.source.snowflake.snowflake_openflow import (
    OpenflowDeploymentKey,
    OpenflowRuntimeKey,
)

PLATFORM = "openflow"
DEPLOYMENT_KEY = "hq8crgi3"
RUNTIME_KEY = "ingestiontest-100"


def test_deployment_container_urn_is_stable():
    key = OpenflowDeploymentKey(
        platform=PLATFORM, env="PROD", deployment=DEPLOYMENT_KEY
    )
    assert key.as_urn() == "urn:li:container:3b68cd54ac7416561c6ec600e0d08fdc"


def test_runtime_container_urn_is_stable():
    key = OpenflowRuntimeKey(
        platform=PLATFORM, env="PROD", deployment=DEPLOYMENT_KEY, runtime=RUNTIME_KEY
    )
    assert key.as_urn() == "urn:li:container:1a7caca9dd5bccc6ca77c016a2375f3a"


def test_env_does_not_affect_the_container_guid():
    # ContainerKey.guid_dict() excludes env, so a PROD and a DEV deployment with
    # the same key are the SAME container. This is inherited behaviour, asserted
    # here so nobody "fixes" it by adding env to the key and re-keying every URN.
    prod = OpenflowDeploymentKey(
        platform=PLATFORM, env="PROD", deployment=DEPLOYMENT_KEY
    )
    dev = OpenflowDeploymentKey(platform=PLATFORM, env="DEV", deployment=DEPLOYMENT_KEY)
    assert prod.as_urn() == dev.as_urn()


def test_runtime_key_resolves_its_parent_deployment():
    runtime = OpenflowRuntimeKey(
        platform=PLATFORM, env="PROD", deployment=DEPLOYMENT_KEY, runtime=RUNTIME_KEY
    )
    parent = runtime.parent_key()
    assert parent is not None
    assert parent.as_urn() == "urn:li:container:3b68cd54ac7416561c6ec600e0d08fdc"


def test_platform_instance_changes_the_urn():
    # Two Snowflake accounts both have a deployment keyed hq8crgi3; without a
    # platform_instance their containers would merge.
    plain = OpenflowDeploymentKey(
        platform=PLATFORM, env="PROD", deployment=DEPLOYMENT_KEY
    )
    scoped = OpenflowDeploymentKey(
        platform=PLATFORM, env="PROD", instance="acct1", deployment=DEPLOYMENT_KEY
    )
    assert plain.as_urn() != scoped.as_urn()

from datahub.ingestion.source.common.subtypes import (
    DataFlowSubTypes,
    DataJobSubTypes,
    GenericContainerSubTypes,
    SourceCapabilityModifier,
)


def test_openflow_container_subtypes_exist():
    assert GenericContainerSubTypes.OPENFLOW_DEPLOYMENT == "Openflow Deployment"
    assert GenericContainerSubTypes.OPENFLOW_RUNTIME == "Openflow Runtime"


def test_openflow_flow_and_job_subtypes_exist():
    assert DataFlowSubTypes.OPENFLOW_CONNECTOR == "Openflow Connector"
    assert DataJobSubTypes.OPENFLOW_CONNECTOR_SYNC == "Openflow Connector Sync"


def test_subtypes_generate_capability_modifiers():
    # SourceCapabilityModifier is generated from the subtype enums, so the
    # @capability decorators in the source module only work once the subtypes
    # above exist. Distinct names matter: the generator keeps the first member
    # for a repeated name and silently drops the rest.
    assert SourceCapabilityModifier.OPENFLOW_DEPLOYMENT == "Openflow Deployment"
    assert SourceCapabilityModifier.OPENFLOW_RUNTIME == "Openflow Runtime"
    assert SourceCapabilityModifier.OPENFLOW_CONNECTOR == "Openflow Connector"

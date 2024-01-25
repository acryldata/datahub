from pydantic import Field, validator

from datahub.configuration.common import ConfigModel
from datahub.configuration.validate_host_port import validate_host_port
from datahub.ingestion.api.registry import import_path
from datahub.utilities.oauth_cb_providers.base_oauth_cb_provider import BaseOAuthCbProvider


class _KafkaConnectionConfig(ConfigModel):
    # bootstrap servers
    bootstrap: str = "localhost:9092"

    # schema registry location
    schema_registry_url: str = "http://localhost:8080/schema-registry/api/"

    schema_registry_config: dict = Field(
        default_factory=dict,
        description="Extra schema registry config serialized as JSON. These options will be passed into Kafka's SchemaRegistryClient. https://docs.confluent.io/platform/current/clients/confluent-kafka-python/html/index.html?#schemaregistryclient",
    )

    client_timeout_seconds: int = Field(
        default=60,
        description="The request timeout used when interacting with the Kafka APIs.",
    )

    @validator("bootstrap")
    def bootstrap_host_colon_port_comma(cls, val: str) -> str:
        for entry in val.split(","):
            validate_host_port(entry)
        return val


class KafkaConsumerConnectionConfig(_KafkaConnectionConfig):
    """Configuration class for holding connectivity information for Kafka consumers"""

    consumer_config: dict = Field(
        default_factory=dict,
        description="Extra consumer config serialized as JSON. These options will be passed into Kafka's DeserializingConsumer. See https://docs.confluent.io/platform/current/clients/confluent-kafka-python/html/index.html#deserializingconsumer and https://github.com/edenhill/librdkafka/blob/master/CONFIGURATION.md .",
    )

    if "oauth_cb_provider_class" in consumer_config:
        oauth_cb_provider_class: BaseOAuthCbProvider = import_path(consumer_config["oauth_cb_provider_class"])
        consumer_config["oauth_cb"] = oauth_cb_provider_class.oauth_cb


class KafkaProducerConnectionConfig(_KafkaConnectionConfig):
    """Configuration class for holding connectivity information for Kafka producers"""

    producer_config: dict = Field(
        default_factory=dict,
        description="Extra producer config serialized as JSON. These options will be passed into Kafka's SerializingProducer. See https://docs.confluent.io/platform/current/clients/confluent-kafka-python/html/index.html#serializingproducer and https://github.com/edenhill/librdkafka/blob/master/CONFIGURATION.md .",
    )

    if "oauth_cb_provider_class" in producer_config:
        oauth_cb_provider_class: BaseOAuthCbProvider = import_path(producer_config["oauth_cb_provider_class"])
        producer_config["oauth_cb"] = oauth_cb_provider_class.oauth_cb


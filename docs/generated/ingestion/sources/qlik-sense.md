


# Qlik Sense

## Overview

Qlik Sense is a business intelligence and analytics platform. Learn more in the [official Qlik Sense documentation](https://www.qlik.com/us/products/qlik-sense).

The DataHub integration for Qlik Sense covers BI entities such as dashboards, charts, datasets, and related ownership context. It also captures table- and column-level lineage, ownership, and stateful deletion detection.

## Concept Mapping

| Qlik Sense | Datahub                                                     | Notes                                                                           |
| ---------- | ----------------------------------------------------------- | ------------------------------------------------------------------------------- |
| `Space`    | [Container](../../metamodel/entities/container.md)          | SubType `"Qlik Space"`. On-prem streams too.                                    |
| `App`      | [Container](../../metamodel/entities/container.md)          | SubType `"Qlik App"`                                                            |
| `Sheet`    | [Dashboard](../../metamodel/entities/dashboard.md)          |                                                                                 |
| `Chart`    | [Chart](../../metamodel/entities/chart.md)                  |                                                                                 |
| `Dataset`  | [Dataset](../../metamodel/entities/dataset.md)              | SubType `"Qlik Dataset"`. Qlik Cloud catalog datasets (data files in a space).  |
| App table  | [Dataset](../../metamodel/entities/dataset.md)              | SubType `"Qlik Table"`. Tables in an app's data model and from its load script. |
| `User`     | [User (aka CorpUser)](../../metamodel/entities/corpuser.md) | Optionally Extracted                                                            |


## Module `qlik-sense`
![Alpha](https://img.shields.io/badge/support%20status-Alpha-lightgrey)


### Important Capabilities
| Capability | Status | Notes |
| ---------- | ------ | ----- |
| Asset Containers | ✅ | Enabled by default. |
| Column-level Lineage | ✅ | Disabled by default. |
| Descriptions | ✅ | Enabled by default. |
| [Detect Deleted Entities](../../../../metadata-ingestion/docs/dev_guides/stateful.md#stale-entity-removal) | ✅ | Enabled by default via stateful ingestion. |
| Extract Ownership | ✅ | Enabled by default, configured using `ingest_owner`. |
| [Platform Instance](../../../platform-instances.md) | ✅ | Enabled by default. |
| Schema Metadata | ✅ | Enabled by default. |
| Table-Level Lineage | ✅ | Enabled by default. |
| Test Connection | ✅ | Enabled by default. |

### Overview

The `qlik-sense` module ingests metadata from Qlik Sense into DataHub. It is intended for production ingestion workflows and module-specific capabilities are documented below.

This source extracts the following:

- Accessible spaces and apps within that spaces as Container.
- Qlik Datasets as Datahub Datasets with schema metadata.
- Tables in each app's data model, and tables read from its load script, as DataHub Datasets with subtype `Qlik Table`, with schema metadata and upstream lineage when available.
- Sheets as Datahub dashboard and charts present inside sheets.
- On-prem streams, apps, sheets, and charts when the `on_prem` block is set.

### Prerequisites

Before running ingestion, ensure network connectivity to the source, valid authentication credentials, and read permissions for metadata APIs required by this module.

#### Qlik Cloud

1. Refer [doc](https://qlik.dev/authenticate/api-key/generate-your-first-api-key/) to generate an API key from the hub.
2. Get tenant hostname from About tab after login to qlik sense account.

#### Qlik Sense Enterprise on Windows

Set the `on_prem` block instead of `tenant_hostname` and `api_key`. Export the client certificate, client key, and root certificate from the QMC as PEM files. `client_cert`, `client_key`, and `root_cert` are file paths, not file contents, so the files must exist on the machine that runs ingestion. When the source is set up in the DataHub UI, run it on an executor where the files are mounted (on DataHub Cloud, a remote executor). The ingestion host must reach the Repository Service (port 4242) and the Engine (port 4747) on the central node. Certificate calls run with administrator rights. The default accounts are `INTERNAL\sa_repository` for inventory and `INTERNAL\sa_engine` for app content. Set `client_key_password` when the client key was exported with a password. Test connection checks both ports. Each Repository Service request and each Engine connection or reply waits up to `timeout_seconds` (default 300), and an app whose read times out is ingested as partly read.

This mode ingests streams, apps, sheets, and charts, plus lineage from the `SELECT` statements in each app's load script. It does not ingest Qlik Cloud datasets or QVD lineage. In a published app, users' private sheets are skipped, as on Qlik Cloud; base and community sheets are ingested. Owners are ingested by their Qlik user id, which for Windows directories is the account name, so they join with users ingested from Active Directory or LDAP by username.

The server certificate is verified against `root_cert`. Qlik issues it to the central node's hostname, so if you connect by IP address or an alias, set `server_cert_hostname` to the name on the certificate. The certificate chain is still verified when `verify_server_hostname` is `false`, but any certificate signed by that root is accepted for any host.

:::caution
`verify_server_hostname: false` leaves the connection open to impersonation by another server holding a certificate from the same root. Use it only as a last resort, after trying `server_cert_hostname`, a DNS entry that matches the certificate, or a reissued certificate.
:::

```yaml
source:
  type: qlik-sense
  config:
    on_prem:
      server: 10.0.0.10
      server_cert_hostname: qlik-central.example.com
      client_cert: /secrets/client.pem
      client_key: /secrets/client_key.pem
      root_cert: /secrets/root.pem
```


### Install the Plugin
```shell
pip install 'acryl-datahub[qlik-sense]'
```

### Starter Recipe
Check out the following recipe to get started with ingestion! See [below](#config-details) for full configuration options.


For general pointers on writing and running a recipe, see our [main recipe guide](../../../../metadata-ingestion/README.md#recipes).
```yaml
source:
  type: qlik-sense
  config:
    # Coordinates
    tenant_hostname: "xyz12xz.us.qlikcloud.com"
    # Credentials
    api_key: "QLIK_API_KEY"

    # Qlik Sense Enterprise on Windows: set on_prem instead of tenant_hostname and api_key.
    # on_prem:
    #   server: qlik-central.example.com
    #   client_cert: /secrets/client.pem
    #   client_key: /secrets/client_key.pem
    #   root_cert: /secrets/root.pem
    
    # Optional - filter for certain space names instead of ingesting everything.
    # Mention 'personal_space' if entities of personal space need to ingest
    # space_pattern:
    #   allow:
    #     - space_name

    ingest_owner: true
    
    # Optional -- This mapping is optional and only required to configure platform-instance for Qlik app dataset upstream source tables
    # A mapping of the Qlik app dataset upstream tables from data connection to platform instance. Use 'data_connection_name' as key.
    # data_connection_to_platform_instance:
    #   data_connection_name:
    #     platform_instance: cloud_instance
    #     env: DEV
    #     platform: mssql
    #     database: sales_db
    #     default_schema: dbo

sink:
  # sink configs

```

### Config Details

                
#### Options


Note that a `.` is used to denote nested fields in the YAML recipe.


<div className='config-table'>

| Field | Description |
|:--- |:--- |
| <div className="path-line"><span className="path-main">api_key</span></div> <div className="type-name-line"><span className="type-name">One of string(password), null</span></div> | Qlik Cloud API key. Required unless `on_prem` is set. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-main">ingest_owner</span></div> <div className="type-name-line"><span className="type-name">One of boolean, null</span></div> | Ingest Owner from source. This will override Owner info entered from UI <div className="default-line default-line-with-docs">Default: <span className="default-value">True</span></div> |
| <div className="path-line"><span className="path-main">max_partial_reads_kept</span></div> <div className="type-name-line"><span className="type-name">integer</span></div> | With stateful ingestion and stale metadata removal, how many consecutive runs an app that is only partly read keeps the entities of its last complete read. After that, its partial read is ingested and reported as a failure, so its current state appears and stale metadata removal is skipped for that run. <div className="default-line default-line-with-docs">Default: <span className="default-value">3</span></div> |
| <div className="path-line"><span className="path-main">max_workers</span></div> <div className="type-name-line"><span className="type-name">One of integer, null</span></div> | Number of apps and datasets read at the same time, across all spaces and streams. Each app read holds one Engine session open. Defaults to 4 on-prem, where apps are opened without their data, and to 1 on Qlik Cloud, where every open app's data is loaded. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-main">platform_instance</span></div> <div className="type-name-line"><span className="type-name">One of string, null</span></div> | The instance of the platform that all assets produced by this recipe belong to. This should be unique within the platform. See https://docs.datahub.com/docs/platform-instances/ for more details. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-main">tenant_hostname</span></div> <div className="type-name-line"><span className="type-name">One of string, null</span></div> | Qlik Cloud tenant hostname. Required unless `on_prem` is set. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-main">timeout_seconds</span></div> <div className="type-name-line"><span className="type-name">integer</span></div> | Seconds to wait for the Qlik Engine, and on-prem for the Repository Service, to connect or reply before the request fails. An app whose read times out is ingested as partly read. <div className="default-line default-line-with-docs">Default: <span className="default-value">300</span></div> |
| <div className="path-line"><span className="path-main">env</span></div> <div className="type-name-line"><span className="type-name">string</span></div> | The environment that all assets produced by this connector belong to <div className="default-line default-line-with-docs">Default: <span className="default-value">PROD</span></div> |
| <div className="path-line"><span className="path-main">data_connection_to_platform_instance</span></div> <div className="type-name-line"><span className="type-name">map(str,PlatformDetail)</span></div> |   |
| <div className="path-line"><span className="path-prefix">data_connection_to_platform_instance.`key`.</span><span className="path-main">env</span></div> <div className="type-name-line"><span className="type-name">string</span></div> | The environment that all assets produced by this connector belong to <div className="default-line default-line-with-docs">Default: <span className="default-value">PROD</span></div> |
| <div className="path-line"><span className="path-prefix">data_connection_to_platform_instance.`key`.</span><span className="path-main">database</span></div> <div className="type-name-line"><span className="type-name">One of string, null</span></div> | Database (catalog for Databricks) used for tables the load script does not qualify. Inferred from the connection string when not set. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-prefix">data_connection_to_platform_instance.`key`.</span><span className="path-main">default_schema</span></div> <div className="type-name-line"><span className="type-name">One of string, null</span></div> | Schema used for tables the load script does not qualify. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-prefix">data_connection_to_platform_instance.`key`.</span><span className="path-main">platform</span></div> <div className="type-name-line"><span className="type-name">One of string, null</span></div> | DataHub platform of the tables read through this connection, e.g. `mssql` or `databricks`. Inferred from the data connection when not set. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-prefix">data_connection_to_platform_instance.`key`.</span><span className="path-main">platform_instance</span></div> <div className="type-name-line"><span className="type-name">One of string, null</span></div> | The instance of the platform that all assets produced by this recipe belong to. This should be unique within the platform. See https://docs.datahub.com/docs/platform-instances/ for more details. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-main">on_prem</span></div> <div className="type-name-line"><span className="type-name">One of QlikOnPremConfig, null</span></div> | Connection to Qlik Sense Enterprise on Windows. Set this instead of `tenant_hostname` and `api_key` to read an on-prem site. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">client_cert</span>&nbsp;<abbr title="Required if on_prem is set">❓</abbr></div> <div className="type-name-line"><span className="type-name">string</span></div> | Path to the PEM client certificate exported from the QMC.  |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">client_key</span>&nbsp;<abbr title="Required if on_prem is set">❓</abbr></div> <div className="type-name-line"><span className="type-name">string</span></div> | Path to the PEM client key exported from the QMC.  |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">root_cert</span>&nbsp;<abbr title="Required if on_prem is set">❓</abbr></div> <div className="type-name-line"><span className="type-name">string</span></div> | Path to the PEM root certificate exported from the QMC.  |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">server</span>&nbsp;<abbr title="Required if on_prem is set">❓</abbr></div> <div className="type-name-line"><span className="type-name">string</span></div> | Qlik Sense Enterprise hostname. Must match the certificate name. Repository Service and Engine ports are reached on this host.  |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">client_key_password</span></div> <div className="type-name-line"><span className="type-name">One of string(password), null</span></div> | Password of the client key, when it was exported from the QMC with one. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">engine_port</span></div> <div className="type-name-line"><span className="type-name">integer</span></div> | Qlik Engine port used for app, sheet, and chart reads. <div className="default-line default-line-with-docs">Default: <span className="default-value">4747</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">engine_user_id</span></div> <div className="type-name-line"><span className="type-name">string</span></div> | User id for Engine websocket calls. Certificate auth defaults to the engine service account. <div className="default-line default-line-with-docs">Default: <span className="default-value">sa&#95;engine</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">ingest_unpublished_apps</span></div> <div className="type-name-line"><span className="type-name">boolean</span></div> | Also ingest apps that are not published to a stream. <div className="default-line default-line-with-docs">Default: <span className="default-value">False</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">qrs_port</span></div> <div className="type-name-line"><span className="type-name">integer</span></div> | Qlik Repository Service port used for inventory. <div className="default-line default-line-with-docs">Default: <span className="default-value">4242</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">qrs_user_id</span></div> <div className="type-name-line"><span className="type-name">string</span></div> | User id for Repository Service calls. Certificate auth defaults to the repository service account. <div className="default-line default-line-with-docs">Default: <span className="default-value">sa&#95;repository</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">server_cert_hostname</span></div> <div className="type-name-line"><span className="type-name">One of string, null</span></div> | Hostname the Qlik server certificate was issued to, when it differs from `server` (for example when connecting by IP address). The certificate is still verified against `root_cert`, but its name is checked against this value. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">skip_unchanged_apps</span></div> <div className="type-name-line"><span className="type-name">boolean</span></div> | Do not re-read an app from the Engine when the app, its objects (sheets, stories, master items), its reload time, the data connections, this recipe and the connector version are all unchanged since the last run, and DataHub still holds that run's metadata for it. Its entities are kept, not soft-deleted. Requires stateful ingestion. Set `stateful_ingestion.ignore_old_state` for one run to force a full read, for example after ingesting upstream schemas that column-level lineage should resolve against. <div className="default-line default-line-with-docs">Default: <span className="default-value">False</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">user_directory</span></div> <div className="type-name-line"><span className="type-name">string</span></div> | User directory for the X-Qlik-User header. <div className="default-line default-line-with-docs">Default: <span className="default-value">INTERNAL</span></div> |
| <div className="path-line"><span className="path-prefix">on_prem.</span><span className="path-main">verify_server_hostname</span></div> <div className="type-name-line"><span className="type-name">boolean</span></div> | Set to false to skip the certificate name check when no hostname matches it. The certificate chain is still verified against `root_cert`. <div className="default-line default-line-with-docs">Default: <span className="default-value">True</span></div> |
| <div className="path-line"><span className="path-main">space_pattern</span></div> <div className="type-name-line"><span className="type-name">AllowDenyPattern</span></div> | A class to store allow deny regexes. <br />  <br /> Patterns are matched against the start of the string only, not the entire <br /> string - a pattern does not need to match to the end to be considered a match. <br /> For example, the pattern "prod" matches "prod", "prod_east", and "production". <br /> To require an exact match, anchor your pattern explicitly, e.g. "^prod$".  |
| <div className="path-line"><span className="path-prefix">space_pattern.</span><span className="path-main">ignoreCase</span></div> <div className="type-name-line"><span className="type-name">One of boolean, null</span></div> | Whether to ignore case sensitivity during pattern matching. <div className="default-line default-line-with-docs">Default: <span className="default-value">True</span></div> |
| <div className="path-line"><span className="path-main">stateful_ingestion</span></div> <div className="type-name-line"><span className="type-name">One of StatefulStaleMetadataRemovalConfig, null</span></div> | Configuration for stateful ingestion and stale metadata removal. <div className="default-line default-line-with-docs">Default: <span className="default-value">None</span></div> |
| <div className="path-line"><span className="path-prefix">stateful_ingestion.</span><span className="path-main">enabled</span></div> <div className="type-name-line"><span className="type-name">boolean</span></div> | Whether or not to enable stateful ingest. Default: True if a pipeline_name is set and either a datahub-rest sink or `datahub_api` is specified, otherwise False <div className="default-line default-line-with-docs">Default: <span className="default-value">False</span></div> |
| <div className="path-line"><span className="path-prefix">stateful_ingestion.</span><span className="path-main">fail_safe_threshold</span></div> <div className="type-name-line"><span className="type-name">number</span></div> | Prevents large amount of soft deletes & the state from committing from accidental changes to the source configuration if the relative change percent in entities compared to the previous state is above the 'fail_safe_threshold'. <div className="default-line default-line-with-docs">Default: <span className="default-value">75.0</span></div> |
| <div className="path-line"><span className="path-prefix">stateful_ingestion.</span><span className="path-main">remove_stale_metadata</span></div> <div className="type-name-line"><span className="type-name">boolean</span></div> | Soft-deletes the entities present in the last successful run but missing in the current run with stateful_ingestion enabled. <div className="default-line default-line-with-docs">Default: <span className="default-value">True</span></div> |

</div>




#### Schema


The [JSONSchema](https://json-schema.org/) for this configuration is inlined below.


```javascript
{
  "$defs": {
    "AllowDenyPattern": {
      "additionalProperties": false,
      "description": "A class to store allow deny regexes.\n\nPatterns are matched against the start of the string only, not the entire\nstring - a pattern does not need to match to the end to be considered a match.\nFor example, the pattern \"prod\" matches \"prod\", \"prod_east\", and \"production\".\nTo require an exact match, anchor your pattern explicitly, e.g. \"^prod$\".",
      "properties": {
        "allow": {
          "default": [
            ".*"
          ],
          "description": "List of regex patterns to include in ingestion. Patterns match from the start of the string only, not the entire string - anchor with '^...$' for an exact match, e.g. '^prod$'.",
          "items": {
            "type": "string"
          },
          "title": "Allow",
          "type": "array"
        },
        "deny": {
          "default": [],
          "description": "List of regex patterns to exclude from ingestion. Patterns match from the start of the string only, not the entire string - anchor with '^...$' for an exact match, e.g. '^prod$'.",
          "items": {
            "type": "string"
          },
          "title": "Deny",
          "type": "array"
        },
        "ignoreCase": {
          "anyOf": [
            {
              "type": "boolean"
            },
            {
              "type": "null"
            }
          ],
          "default": true,
          "description": "Whether to ignore case sensitivity during pattern matching.",
          "title": "Ignorecase"
        }
      },
      "title": "AllowDenyPattern",
      "type": "object"
    },
    "PlatformDetail": {
      "additionalProperties": false,
      "properties": {
        "env": {
          "default": "PROD",
          "description": "The environment that all assets produced by this connector belong to",
          "title": "Env",
          "type": "string"
        },
        "platform_instance": {
          "anyOf": [
            {
              "type": "string"
            },
            {
              "type": "null"
            }
          ],
          "default": null,
          "description": "The instance of the platform that all assets produced by this recipe belong to. This should be unique within the platform. See https://docs.datahub.com/docs/platform-instances/ for more details.",
          "title": "Platform Instance"
        },
        "platform": {
          "anyOf": [
            {
              "type": "string"
            },
            {
              "type": "null"
            }
          ],
          "default": null,
          "description": "DataHub platform of the tables read through this connection, e.g. `mssql` or `databricks`. Inferred from the data connection when not set.",
          "title": "Platform"
        },
        "database": {
          "anyOf": [
            {
              "type": "string"
            },
            {
              "type": "null"
            }
          ],
          "default": null,
          "description": "Database (catalog for Databricks) used for tables the load script does not qualify. Inferred from the connection string when not set.",
          "title": "Database"
        },
        "default_schema": {
          "anyOf": [
            {
              "type": "string"
            },
            {
              "type": "null"
            }
          ],
          "default": null,
          "description": "Schema used for tables the load script does not qualify.",
          "title": "Default Schema"
        }
      },
      "title": "PlatformDetail",
      "type": "object"
    },
    "QlikOnPremConfig": {
      "additionalProperties": false,
      "properties": {
        "server": {
          "description": "Qlik Sense Enterprise hostname. Must match the certificate name. Repository Service and Engine ports are reached on this host.",
          "minLength": 1,
          "title": "Server",
          "type": "string"
        },
        "client_cert": {
          "description": "Path to the PEM client certificate exported from the QMC.",
          "minLength": 1,
          "title": "Client Cert",
          "type": "string"
        },
        "client_key": {
          "description": "Path to the PEM client key exported from the QMC.",
          "minLength": 1,
          "title": "Client Key",
          "type": "string"
        },
        "root_cert": {
          "description": "Path to the PEM root certificate exported from the QMC.",
          "minLength": 1,
          "title": "Root Cert",
          "type": "string"
        },
        "client_key_password": {
          "anyOf": [
            {
              "format": "password",
              "type": "string",
              "writeOnly": true
            },
            {
              "type": "null"
            }
          ],
          "default": null,
          "description": "Password of the client key, when it was exported from the QMC with one.",
          "title": "Client Key Password"
        },
        "qrs_port": {
          "default": 4242,
          "description": "Qlik Repository Service port used for inventory.",
          "maximum": 65535,
          "minimum": 1,
          "title": "Qrs Port",
          "type": "integer"
        },
        "engine_port": {
          "default": 4747,
          "description": "Qlik Engine port used for app, sheet, and chart reads.",
          "maximum": 65535,
          "minimum": 1,
          "title": "Engine Port",
          "type": "integer"
        },
        "server_cert_hostname": {
          "anyOf": [
            {
              "type": "string"
            },
            {
              "type": "null"
            }
          ],
          "default": null,
          "description": "Hostname the Qlik server certificate was issued to, when it differs from `server` (for example when connecting by IP address). The certificate is still verified against `root_cert`, but its name is checked against this value.",
          "title": "Server Cert Hostname"
        },
        "verify_server_hostname": {
          "default": true,
          "description": "Set to false to skip the certificate name check when no hostname matches it. The certificate chain is still verified against `root_cert`.",
          "title": "Verify Server Hostname",
          "type": "boolean"
        },
        "user_directory": {
          "default": "INTERNAL",
          "description": "User directory for the X-Qlik-User header.",
          "title": "User Directory",
          "type": "string"
        },
        "qrs_user_id": {
          "default": "sa_repository",
          "description": "User id for Repository Service calls. Certificate auth defaults to the repository service account.",
          "title": "Qrs User Id",
          "type": "string"
        },
        "engine_user_id": {
          "default": "sa_engine",
          "description": "User id for Engine websocket calls. Certificate auth defaults to the engine service account.",
          "title": "Engine User Id",
          "type": "string"
        },
        "ingest_unpublished_apps": {
          "default": false,
          "description": "Also ingest apps that are not published to a stream.",
          "title": "Ingest Unpublished Apps",
          "type": "boolean"
        },
        "skip_unchanged_apps": {
          "default": false,
          "description": "Do not re-read an app from the Engine when the app, its objects (sheets, stories, master items), its reload time, the data connections, this recipe and the connector version are all unchanged since the last run, and DataHub still holds that run's metadata for it. Its entities are kept, not soft-deleted. Requires stateful ingestion. Set `stateful_ingestion.ignore_old_state` for one run to force a full read, for example after ingesting upstream schemas that column-level lineage should resolve against.",
          "title": "Skip Unchanged Apps",
          "type": "boolean"
        }
      },
      "required": [
        "server",
        "client_cert",
        "client_key",
        "root_cert"
      ],
      "title": "QlikOnPremConfig",
      "type": "object"
    },
    "StatefulStaleMetadataRemovalConfig": {
      "additionalProperties": false,
      "description": "Base specialized config for Stateful Ingestion with stale metadata removal capability.",
      "properties": {
        "enabled": {
          "default": false,
          "description": "Whether or not to enable stateful ingest. Default: True if a pipeline_name is set and either a datahub-rest sink or `datahub_api` is specified, otherwise False",
          "title": "Enabled",
          "type": "boolean"
        },
        "remove_stale_metadata": {
          "default": true,
          "description": "Soft-deletes the entities present in the last successful run but missing in the current run with stateful_ingestion enabled.",
          "title": "Remove Stale Metadata",
          "type": "boolean"
        },
        "fail_safe_threshold": {
          "default": 75.0,
          "description": "Prevents large amount of soft deletes & the state from committing from accidental changes to the source configuration if the relative change percent in entities compared to the previous state is above the 'fail_safe_threshold'.",
          "maximum": 100.0,
          "minimum": 0.0,
          "title": "Fail Safe Threshold",
          "type": "number"
        }
      },
      "title": "StatefulStaleMetadataRemovalConfig",
      "type": "object"
    }
  },
  "additionalProperties": false,
  "properties": {
    "env": {
      "default": "PROD",
      "description": "The environment that all assets produced by this connector belong to",
      "title": "Env",
      "type": "string"
    },
    "platform_instance": {
      "anyOf": [
        {
          "type": "string"
        },
        {
          "type": "null"
        }
      ],
      "default": null,
      "description": "The instance of the platform that all assets produced by this recipe belong to. This should be unique within the platform. See https://docs.datahub.com/docs/platform-instances/ for more details.",
      "title": "Platform Instance"
    },
    "stateful_ingestion": {
      "anyOf": [
        {
          "$ref": "#/$defs/StatefulStaleMetadataRemovalConfig"
        },
        {
          "type": "null"
        }
      ],
      "default": null,
      "description": "Configuration for stateful ingestion and stale metadata removal."
    },
    "tenant_hostname": {
      "anyOf": [
        {
          "type": "string"
        },
        {
          "type": "null"
        }
      ],
      "default": null,
      "description": "Qlik Cloud tenant hostname. Required unless `on_prem` is set.",
      "title": "Tenant Hostname"
    },
    "api_key": {
      "anyOf": [
        {
          "format": "password",
          "type": "string",
          "writeOnly": true
        },
        {
          "type": "null"
        }
      ],
      "default": null,
      "description": "Qlik Cloud API key. Required unless `on_prem` is set.",
      "title": "Api Key"
    },
    "on_prem": {
      "anyOf": [
        {
          "$ref": "#/$defs/QlikOnPremConfig"
        },
        {
          "type": "null"
        }
      ],
      "default": null,
      "description": "Connection to Qlik Sense Enterprise on Windows. Set this instead of `tenant_hostname` and `api_key` to read an on-prem site."
    },
    "max_partial_reads_kept": {
      "default": 3,
      "description": "With stateful ingestion and stale metadata removal, how many consecutive runs an app that is only partly read keeps the entities of its last complete read. After that, its partial read is ingested and reported as a failure, so its current state appears and stale metadata removal is skipped for that run.",
      "minimum": 0,
      "title": "Max Partial Reads Kept",
      "type": "integer"
    },
    "max_workers": {
      "anyOf": [
        {
          "minimum": 1,
          "type": "integer"
        },
        {
          "type": "null"
        }
      ],
      "default": null,
      "description": "Number of apps and datasets read at the same time, across all spaces and streams. Each app read holds one Engine session open. Defaults to 4 on-prem, where apps are opened without their data, and to 1 on Qlik Cloud, where every open app's data is loaded.",
      "title": "Max Workers"
    },
    "space_pattern": {
      "$ref": "#/$defs/AllowDenyPattern",
      "default": {
        "allow": [
          ".*"
        ],
        "deny": [],
        "ignoreCase": true
      },
      "description": "Regex patterns to filter Qlik spaces in ingestion.Mention 'personal_space' if entities of personal space need to ingest"
    },
    "ingest_owner": {
      "anyOf": [
        {
          "type": "boolean"
        },
        {
          "type": "null"
        }
      ],
      "default": true,
      "description": "Ingest Owner from source. This will override Owner info entered from UI",
      "title": "Ingest Owner"
    },
    "timeout_seconds": {
      "default": 300,
      "description": "Seconds to wait for the Qlik Engine, and on-prem for the Repository Service, to connect or reply before the request fails. An app whose read times out is ingested as partly read.",
      "minimum": 1,
      "title": "Timeout Seconds",
      "type": "integer"
    },
    "data_connection_to_platform_instance": {
      "additionalProperties": {
        "$ref": "#/$defs/PlatformDetail"
      },
      "default": {},
      "description": "A mapping of the Qlik app dataset upstream tables from data connection to platform instance.Use 'data_connection_name' as key. Set `platform`, `database` and `default_schema` when they cannot be inferred from the data connection.",
      "title": "Data Connection To Platform Instance",
      "type": "object"
    }
  },
  "title": "QlikSourceConfig",
  "type": "object"
}
```





### Capabilities

Use the **Important Capabilities** table above as the source of truth for supported features and whether additional configuration is required.

#### Lineage from load scripts

On-prem, the connector always reads each app's load script. On Qlik Cloud it reads the script when the app's data model has no tables, or when some of its tables have no upstream lineage, for example apps loaded through the data load editor. Tables already traced by the data model are not added again from the script. A `SELECT` that runs after `LIB CONNECT TO`, `ODBC CONNECT TO`, or `OLEDB CONNECT TO` becomes a Qlik table with lineage to the tables it reads, as long as its data connection resolves to a platform and the SQL can be parsed. The upstream platform and database come from the data connection. Use `data_connection_to_platform_instance` when they cannot be inferred, or when the upstream source uses a platform instance or env:

```yaml
data_connection_to_platform_instance:
  "My SQL Server connection":
    platform: mssql
    database: sales_db
    default_schema: dbo
  "My Databricks connection":
    platform: databricks
    database: main
```

Variables set with `SET`, or with `LET` and a quoted string, are expanded in connection names and SQL. Variables set with `LET` from an expression, and scripts pulled in with `$(Include=...)` or `$(Must_Include=...)`, are not read. After an include, the connection and all variables are treated as unknown. A `SUB` body is read where the script calls it, with the connection and variables in effect at that `CALL`, and a `SUB` that is never called adds no tables. Quoted or numeric arguments of a `CALL` are bound to the `SUB` parameters for that call, and any other argument leaves its parameter unknown. The variable of a `FOR` or `FOR EACH` loop is unknown inside the loop. Nothing after an unconditional `EXIT SCRIPT` is read. An unlabelled `SELECT` table is named after the table that follows `FROM`, cut to 32 characters, as Qlik names it. `--` comments in SQL are ignored. A `CALL` of a `SUB` the script does not define makes the connection and all variables unknown, as an include does. After either, the table loaded last is unknown too, so a `Concatenate` or `Join` without a table name is skipped until another table is loaded. A `SELECT` that runs while the connection is unknown is skipped. Rows added with `Concatenate (Table)` or fields added with `Join (Table)` belong to the named table, and without a table name to the table loaded just before. Conditions are not evaluated: a connection or variable set inside an `IF` or `SWITCH` block is kept after the block only when every branch sets the same value. An `IF` without `ELSE` or a `SWITCH` without `DEFAULT` can run no branch, so the value from before the block must match as well.

A `LOAD` stacked on a `SELECT` (a preceding load) gives the table its field names: `amount AS [Net Sales]` becomes field `Net Sales` with column lineage from `amount`, and an expression such as `Year(order_date) AS Year` gets column lineage from every column it reads. `LOAD *` keeps the `SELECT` columns, and so does a preceding load with a field list item that is a `$(variable)` on its own, aliased or not (such as `LOAD $(vFields)` or `$(vField) AS Name`), or whose field name is a `$(variable)` (such as `amount AS [$(vName)]`), since the variable may hold any of them. A field aliased from such a variable, `Name` here, gets no column lineage, and a field named by a variable is left out. A variable inside a field's expression, as in `amount * $(vRate) AS Net`, only changes that field's value. The fields it does name are still added. Unquoted field names may start with `%`, as Qlik key fields often do. A field from an expression without `AS` is left out, since Qlik names it after the expression text. A table loaded with `RESIDENT` from another script table gets no lineage. Source columns are matched by exact name first and then in any case, since the SQL parser may change their case; when two columns differ only in case, a name in a third case gets no column lineage. A `CrossTable` load keeps column lineage for its qualifier fields (the first fields, one unless its third argument says otherwise); its data field gets lineage from the columns it turns into rows, and its attribute field, which holds column names, gets none. An unaliased expression in its field list still takes its place among the fields, so the qualifiers are counted correctly, but it is not ingested as a field. A `Generic` load, or a `CrossTable` whose field list is not known (such as `LOAD *`), gets table lineage only.

A connection named in `LIB CONNECT TO` resolves through `data_connection_to_platform_instance`, or through a data connection the connector can list, whose platform comes from its connector type or connection string. The platform is never guessed from the connection name, so an unlisted name without a mapping does not resolve. On Qlik Cloud, a space-prefixed name resolves to the connection in that space, and a name without a space prefix resolves only to the connection with that name in the app's own space (the personal space for personal apps), even when another space holds a connection with that name. Map the name in `data_connection_to_platform_instance` when the app's own connection is not visible to the API key. An inline `ODBC CONNECT TO` or `OLEDB CONNECT TO` connection string resolves from its driver or provider. Only the properties that name where the data lives (driver, provider, server, host, port, data source, database, catalog, schema, DSN, warehouse, and account) are kept when an inline connection string is reported or ingested, and URLs in them lose their user name, password, and query string.

A `SELECT` whose connection resolves to no platform is skipped, and the connection is listed under `unresolved_data_connections` in the ingestion report. A `SELECT` that cannot be parsed, for example one that uses an unexpanded variable in a table name, is skipped and reported as a warning. On SQL Server, Snowflake, PostgreSQL, Redshift, Databricks, BigQuery, MySQL, MariaDB, and Oracle, a table whose database or schema is neither in the SQL nor resolved for its connection is left out of lineage and reported as a warning. Set `database` and `default_schema` for the connection to include it.

On Qlik Cloud, only the Owner and users with **Can manage** or **Can operate** in a managed space can read an app's load script. When the Engine denies access to the script, the app is still ingested and the run reports a warning. Charts of an app whose script was denied get their inputs from the data model. Charts of an app whose data model or load script could not be read for another reason keep the inputs from their last read. Any other failure to read the script marks the app as partly read and reports a failure for the run, which keeps stale metadata removal from deleting its lineage.

#### Charts in containers

Charts inside a container object's tabs are ingested as charts of the sheet. An untitled chart takes its tab label as its title. Objects with no data, such as buttons, filter panes and variable inputs, are skipped.

#### Chart input fields

Each chart lists the Qlik table fields it reads as input fields, and its inputs are narrowed to the tables those fields belong to. Fields are taken from the chart's dimensions and measure expressions, including master measures. A name in an expression counts only when it matches a field of one of the app's ingested tables. Names match case-sensitively, as Qlik field names do, except for the columns of tables traced from a load script, whose case the SQL parser may change. Tables loaded with `MAPPING` are only used by mapping operations such as `ApplyMap`, `Map ... Using`, `Rename Field` and `MapSubstring`, and are never chart inputs. A derived field such as `Date.autoCalendar.Year` or `[Date].autoCalendar.Year` counts as its base field `Date`, unless a field named exactly `Date.autoCalendar.Year` exists, which then takes precedence. When neither is known, the chart keeps all of the app's tables as inputs. Values in set modifiers, such as `"Net*"` and `"East"` in `{<Region={"Net*"}+{"East"}>}`, are search strings and are not matched. For tables traced from a load script, those are the columns the SQL parser found, under the names a preceding load gives them. A field shared by several tables, such as a key field, is listed for each of them. When no field matches, a measure cannot be read, or an expression uses a `$(variable)` whose fields are unknown, the chart keeps all of the app's tables as inputs. A chart whose measures could not be read this time keeps the input fields from its last read, while a chart that uses a deleted master measure lists the fields its other dimensions and measures read. Comments in expressions are ignored.

#### Large sites

On-prem, each app is opened in the Engine without its data. On Qlik Cloud, the Engine loads each app's data when the app is opened, so every worker holds an app in memory, and a rate-limited Engine connection is not retried and leaves its app unread. Raise `max_workers` on Qlik Cloud with care. `max_workers` sets how many apps and datasets are read at the same time, across all spaces and streams. It defaults to 4 on-prem and to 1 on Qlik Cloud. Each worker holds its own Engine session under the configured user. `max_workers` applies to Qlik Cloud too, where a rate-limited (HTTP 429) REST request is retried up to 5 times. Each retry waits for the `Retry-After` time when Qlik sends one, and otherwise backs off for 0, 2, 4, 8 and 16 seconds. A request made while listing or reading a space, item or app that is still rate-limited after that fails, and that space, item or app is reported as unread or partly read, which keeps stale metadata removal from deleting its metadata. Lower `max_workers` if this happens.

With stateful ingestion and stale metadata removal enabled, on Qlik Cloud and on-prem, each run records which entities every app produced. When an app is only partly read and its last read was recorded, nothing from the partial read is emitted and the run reports a warning: the app's metadata stays as of its last complete read until a read of it completes. This lasts for at most as many consecutive runs as the top-level `max_partial_reads_kept` option sets (3 by default). After that, the partial read is ingested so the app's current state appears, and the run reports a failure, which skips stale metadata removal for that run. A complete read resets the count. A failed run keeps the recorded last read of the apps it did not reach. When its last read was not recorded, for example on the first run or when the last checkpoint could not be saved, the partial read is emitted and the run reports a failure, which keeps stale metadata removal from deleting anything in that run. A complete read records only what it produced, so sheets and charts removed in Qlik are soft-deleted. The checkpoint holds every entity URN of every app, alongside the stale metadata removal state. If it grows past `stateful_ingestion.max_checkpoint_state_size` (16 MB by default), it is not saved and the run reports a warning; raise the limit.

With `skip_unchanged_apps: true` in the `on_prem` block and stateful ingestion enabled, later runs only open apps that changed in the Engine. This is on-prem only, because the Repository Service reports when each app, sheet, story and master item was last modified, and Qlik Cloud has no equivalent listing. An app is read again when the app or any of its sheets, stories or master items is saved, when it is reloaded, published or moves stream, when its owner or a sheet's owner changes, when the recipe or a data connection changes, or after a connector upgrade. It is also read again if the last read of it failed, if its app container in DataHub is missing or does not carry the fingerprint of that read (stored as the container's `ingestionFingerprint` custom property, which is visible in the UI), or if any entity that read produced is missing from DataHub or soft-deleted. When the change check itself fails, the app is read in full, the run reports a warning, and the app is listed under `apps_change_check_failed`. Aspects that failed to write for an entity that exists are not detected. To read every app again, turn `skip_unchanged_apps` off for one run; `stateful_ingestion.ignore_old_state: true` also does that, but skips stale metadata removal for that run. Entities of a skipped app stay in place and are not soft-deleted.

Column-level lineage is resolved against the upstream schemas in DataHub at read time. After ingesting an upstream source for the first time, turn `skip_unchanged_apps` off for one run so every app is read again.

```yaml
max_workers: 4
on_prem:
  # server and certificates as above
  skip_unchanged_apps: true
stateful_ingestion:
  enabled: true
  remove_stale_metadata: true
```

### Limitations

Module behavior is constrained by source APIs, permissions, and metadata exposed by the platform. Refer to capability notes for unsupported or conditional features.

### Troubleshooting

If ingestion fails, validate credentials, permissions, connectivity, and scope filters first. Then review ingestion logs for source-specific errors and adjust configuration accordingly.


### Code Coordinates
- Class Name: `datahub.ingestion.source.qlik_sense.qlik_sense.QlikSenseSource`
- Browse on [GitHub](https://github.com/datahub-project/datahub/blob/master/metadata-ingestion/src/datahub/ingestion/source/qlik_sense/qlik_sense.py)


:::tip Questions?

If you've got any questions on configuring ingestion for Qlik Sense, feel free to ping us on [our Slack](https://datahub.com/slack).
:::



:::note 💡 **Contributing to this documentation**
This page is auto-generated from the underlying source code. To make changes, please edit the relevant source files in the [metadata-ingestion](https://github.com/datahub-project/datahub/tree/master/metadata-ingestion) directory. 

**Tip:** For quick typo fixes or documentation updates, you can click the ✏️ **Edit** icon directly in the GitHub UI to open a Pull Request. For larger changes and PR naming conventions, please refer to our [Contributing Guide](/docs/contributing).
:::

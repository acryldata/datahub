### Capabilities

- **Containers** — one container per Openflow deployment and runtime, nested.
- **Table-level lineage** — derived from each connector's own configuration, so it is observed
  rather than declared. Disable with `include_openflow_lineage: false`.
- **Ownership** — from each object's `OWNER`.
- **Deletion detection** — via stateful ingestion, using `DELETED_ON` from the `ACCOUNT_USAGE`
  views.

Schema metadata, column-level lineage, profiling and dataset usage are deliberately **not**
supported here; use the `snowflake` source for the destination tables.

### Limitations

- **Gen 2 connectors only.** Gen 1 Openflow connectors are not Snowflake SQL objects, so this
  source cannot see them — and cannot report how many were omitted. If your account runs Gen 1
  exclusively, the connector inventory will be empty.
- **An empty result may be a permissions problem.** `SHOW OPENFLOW …` is privilege-filtered and
  returns **zero rows** with no error when the role lacks `MONITOR`. The ingestion report raises a
  warning in this case rather than reporting success.
- **Column-level lineage is not available.** NiFi processors operate on FlowFiles rather than typed
  SQL, so there is no statement to parse.
- **Only the `SOURCE_SCHEMA` destination schema strategy is supported.** Prefix, Suffix and Pattern
  strategies exist; a connector using one has its lineage skipped and counted in the report rather
  than guessed.
- **Connectors configured with a table pattern** (rather than explicit table names) cannot have
  their tables enumerated from configuration, so they receive connector-level metadata without
  table lineage.
- **`ACCOUNT_USAGE` views lag.** A newly created runtime can take ~20 minutes to appear. The source
  reconciles `SHOW` against the views and treats a `SHOW`-only object as new, so freshly created
  objects are still ingested.

### Troubleshooting

**The ingestion succeeds but no Openflow objects appear.** Almost always a missing `MONITOR` grant
rather than an empty account, because `SHOW OPENFLOW …` is privilege-filtered and returns zero rows
without an error. Check with `SHOW GRANTS TO ROLE <role>` and `SHOW FUTURE GRANTS IN SCHEMA
<db>.<schema>` — remember FUTURE grants appear only in the latter.

**Lineage points at Snowflake tables that do not exist in DataHub.** `snowflake_platform_instance`
and `snowflake_env` must match your `snowflake` recipe exactly. A mismatch produces well-formed URNs
that resolve to nothing, and nothing reports an error.

**Lineage points at upstream tables that do not exist in DataHub.** The same hazard on the other
side of the edge: `source_platform_instance` and `source_env` must match the recipe that ingests the
system a connector reads from (e.g. your `postgres` recipe). Both default to unset /
this source's `env`, which is correct only when that recipe uses no platform instance.

Upstream table and schema names are emitted exactly as the connector's configuration spells them.
`convert_urns_to_lowercase` applies to the destination Snowflake side only, because `postgres`,
`mysql` and `mssql` all preserve identifier case by default — folding the upstream name would point
the edge at a dataset none of those recipes ever wrote. If you do run one of those recipes with
`convert_urns_to_lowercase: true`, set `source_convert_urns_to_lowercase: true` here to match —
DataHub's MSSQL source actively advises enabling it for lineage, so this is a realistic case
rather than a hypothetical one.

One deliberate difference from the `snowflake` source, worth knowing if you set both: this source
folds the destination identifier itself rather than letting the pipeline-level pass do it, because
that pass rewrites every dataset URN in the stream and would take the upstream ones with it. The
pipeline pass also folds the `platform_instance` prefix, whereas this source folds only the
identifier after it. So an **uppercase** `snowflake_platform_instance` here will not match the same
instance written by a `snowflake` recipe that has `convert_urns_to_lowercase: true` spelled out.
Use a lowercase platform instance on both sides and the question does not arise.

**A connector is catalogued but has no lineage.** Either its destination schema strategy is not
`SOURCE_SCHEMA`, or it selects tables by pattern rather than by name. Both are counted in the
ingestion report.

**A connector is catalogued but does not appear under its runtime.** `SHOW OPENFLOW CONNECTORS` is
account-wide while runtimes are privilege-filtered, so a connector can name a runtime this run never
saw. The connector is still ingested, just not nested. Every such connector is counted in
`num_connectors_without_runtime_parent`; the ones whose runtime was excluded by `runtime_pattern` are
counted silently, and the rest also raise a warning — grant `MONITOR` on the runtime.

**`GRANT MONITOR ON OPENFLOW CONNECTOR` fails.** Expected — `MONITOR` is not a valid privilege on
that object type. Grant on the parent runtime instead.

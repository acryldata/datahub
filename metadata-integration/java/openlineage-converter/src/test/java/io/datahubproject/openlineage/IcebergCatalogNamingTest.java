package io.datahubproject.openlineage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import io.datahubproject.openlineage.config.DatahubOpenlineageConfig;
import io.datahubproject.openlineage.converter.OpenLineageToDataHub;
import io.openlineage.client.OpenLineage;
import java.net.URI;
import java.util.Optional;
import org.testng.annotations.Test;

/**
 * Tests for the {@code icebergCatalogNaming} option: datasets carrying an Iceberg REST catalog
 * facet get catalog-scoped logical URNs ({@code <warehouseUri>.<namespace>.<table>}) on the {@code
 * iceberg} platform instead of the bare symlink name, which is ambiguous when multiple REST
 * catalogs share one endpoint (the standard Polaris deployment model).
 */
public class IcebergCatalogNamingTest {

  private static final String POLARIS_URI = "https://polaris:8181/api/catalog";

  private static OpenLineage.Dataset icebergDataset(
      OpenLineage openLineage, String catalog, String symlinkName) {
    return openLineage
        .newInputDatasetBuilder()
        .namespace("s3a://bucket/warehouse")
        .name(catalog + "/db/orders")
        .facets(
            openLineage
                .newDatasetFacetsBuilder()
                .catalog(
                    openLineage
                        .newCatalogDatasetFacetBuilder()
                        .framework("iceberg")
                        .type("rest")
                        .name(catalog)
                        .warehouseUri(catalog)
                        .metadataUri(POLARIS_URI)
                        .source("spark")
                        .build())
                .symlinks(
                    openLineage
                        .newSymlinksDatasetFacetBuilder()
                        .identifiers(
                            java.util.Collections.singletonList(
                                openLineage
                                    .newSymlinksDatasetFacetIdentifiersBuilder()
                                    .type("TABLE")
                                    .namespace(POLARIS_URI)
                                    .name(symlinkName)
                                    .build()))
                        .build())
                .build())
        .build();
  }

  private static OpenLineage.Dataset plainSymlinkDataset(
      OpenLineage openLineage, String catalogFacetType, String warehouseUri) {
    OpenLineage.CatalogDatasetFacetBuilder catalogBuilder =
        openLineage
            .newCatalogDatasetFacetBuilder()
            .framework("iceberg")
            .type(catalogFacetType)
            .name("spark_catalog");
    if (warehouseUri != null) {
      catalogBuilder.warehouseUri(warehouseUri);
    }
    return openLineage
        .newInputDatasetBuilder()
        .namespace("s3a://bucket/warehouse")
        .name("db/orders")
        .facets(
            openLineage
                .newDatasetFacetsBuilder()
                .catalog(catalogBuilder.build())
                .symlinks(
                    openLineage
                        .newSymlinksDatasetFacetBuilder()
                        .identifiers(
                            java.util.Collections.singletonList(
                                openLineage
                                    .newSymlinksDatasetFacetIdentifiersBuilder()
                                    .type("TABLE")
                                    .namespace(POLARIS_URI)
                                    .name("db.orders")
                                    .build()))
                        .build())
                .build())
        .build();
  }

  private static DatahubOpenlineageConfig config(boolean icebergCatalogNaming) {
    return DatahubOpenlineageConfig.builder().icebergCatalogNaming(icebergCatalogNaming).build();
  }

  /**
   * A dataset carrying a TABLE symlink but no catalog facet: the shape of the "ALTER TABLE ... SET
   * TBLPROPERTIES" output the Spark agent emits for Iceberg REST catalog tables (schema, dataSource
   * and symlinks present, catalog facet missing). Optionally with a schema facet and with a
   * configurable symlink namespace, so both the REST-endpoint and storage-path variants can be
   * built.
   */
  private static OpenLineage.Dataset facetLessStubDataset(
      OpenLineage openLineage, boolean withSchemaFacet) {
    return facetLessStubDataset(openLineage, withSchemaFacet, POLARIS_URI);
  }

  private static OpenLineage.Dataset facetLessStubDataset(
      OpenLineage openLineage, boolean withSchemaFacet, String symlinkNamespace) {
    OpenLineage.DatasetFacetsBuilder facetsBuilder =
        openLineage
            .newDatasetFacetsBuilder()
            .symlinks(
                openLineage
                    .newSymlinksDatasetFacetBuilder()
                    .identifiers(
                        java.util.Collections.singletonList(
                            openLineage
                                .newSymlinksDatasetFacetIdentifiersBuilder()
                                .type("TABLE")
                                .namespace(symlinkNamespace)
                                .name("db.orders")
                                .build()))
                    .build());
    if (withSchemaFacet) {
      facetsBuilder.schema(
          openLineage
              .newSchemaDatasetFacetBuilder()
              .fields(
                  java.util.Collections.singletonList(
                      openLineage
                          .newSchemaDatasetFacetFieldsBuilder()
                          .name("id")
                          .type("long")
                          .build()))
              .build());
    }
    return openLineage
        .newOutputDatasetBuilder()
        .namespace("s3a://bucket/warehouse")
        .name("db/orders")
        .facets(facetsBuilder.build())
        .build();
  }

  @Test
  public void testCatalogQualifiedUrn() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            icebergDataset(openLineage, "cz", "db.orders"), config(true));

    assertTrue(urn.isPresent());
    assertEquals(
        urn.get().toString(),
        "urn:li:dataset:(urn:li:dataPlatform:iceberg,cz.db.orders,PROD)",
        "The URN must be catalog-scoped: <warehouseUri>.<namespace>.<table>");
  }

  @Test
  public void testCatalogsSharingEndpointStayDistinct() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    // One Polaris endpoint serving two catalogs: same REST URI, same bare symlink db.orders,
    // but different server-side catalogs (different warehouseUri).
    Optional<com.linkedin.common.urn.DatasetUrn> czUrn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            icebergDataset(openLineage, "cz", "db.orders"), config(true));
    Optional<com.linkedin.common.urn.DatasetUrn> lzUrn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            icebergDataset(openLineage, "lz", "db.orders"), config(true));

    assertEquals(
        lzUrn.get().toString(), "urn:li:dataset:(urn:li:dataPlatform:iceberg,lz.db.orders,PROD)");
    assertNotEquals(
        czUrn.get(), lzUrn.get(), "Distinct catalogs sharing one endpoint must not collapse");
  }

  @Test
  public void testStoragePathIsAliasedToLogicalUrn() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    DatahubOpenlineageConfig mappingConfig = config(true);
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            icebergDataset(openLineage, "cz", "db.orders"), mappingConfig);

    assertTrue(urn.isPresent());
    assertFalse(
        mappingConfig.getUrnAliases().isEmpty(),
        "The storage-path URN should be aliased to the logical URN so path-based references resolve");
    assertTrue(
        mappingConfig.getUrnAliases().containsValue(urn.get().toString()),
        "Alias target must be the catalog-scoped logical URN");
  }

  @Test
  public void testFlagOffKeepsSymlinkResolution() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            icebergDataset(openLineage, "cz", "db.orders"), config(false));

    assertTrue(urn.isPresent());
    assertEquals(
        urn.get().toString(),
        "urn:li:dataset:(urn:li:dataPlatform:hive,db.orders,PROD)",
        "Without the option, default symlink resolution applies unchanged");
  }

  @Test
  public void testNonRestCatalogTypeFallsBackToSymlinkResolution() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    // Glue and Hive catalogs also emit framework=iceberg; only REST catalogs are re-named.
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            plainSymlinkDataset(openLineage, "glue", "s3://bucket/warehouse"), config(true));

    assertTrue(urn.isPresent());
    assertEquals(
        urn.get().toString(),
        "urn:li:dataset:(urn:li:dataPlatform:hive,db.orders,PROD)",
        "Non-REST catalog types must keep default symlink resolution");
  }

  @Test
  public void testMissingWarehouseUriFallsBackToSymlinkResolution() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            plainSymlinkDataset(openLineage, "rest", null), config(true));

    assertTrue(urn.isPresent());
    assertEquals(
        urn.get().toString(),
        "urn:li:dataset:(urn:li:dataPlatform:hive,db.orders,PROD)",
        "Without a warehouseUri there is no catalog identity to qualify with");
  }

  @Test
  public void testLowerCaseDatasetUrnsAppliesToTableName() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    DatahubOpenlineageConfig mappingConfig =
        DatahubOpenlineageConfig.builder()
            .icebergCatalogNaming(true)
            .lowerCaseDatasetUrns(true)
            .build();
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            icebergDataset(openLineage, "cz", "DB.Orders"), mappingConfig);

    assertTrue(urn.isPresent());
    assertEquals(
        urn.get().toString(),
        "urn:li:dataset:(urn:li:dataPlatform:iceberg,cz.db.orders,PROD)",
        "lowerCaseDatasetUrns must apply to the logical table name");
  }

  @Test
  public void testFacetLessStubIsDropped() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    // A TABLE symlink pointing at the REST endpoint with no catalog facet cannot be resolved to
    // a catalog-scoped URN; its bare name is cross-catalog ambiguous, so it must be dropped.
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            facetLessStubDataset(openLineage, false), config(true));

    assertFalse(urn.isPresent(), "A REST-symlink dataset without a catalog facet must be dropped");
  }

  @Test
  public void testAlterTableOutputShapeIsDropped() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    // The real-world shape observed for "ALTER TABLE ... SET TBLPROPERTIES" on an Iceberg REST
    // catalog table: full schema facet present, catalog facet missing. It must still be dropped:
    // the schema does not disambiguate which shared-endpoint catalog the table belongs to.
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            facetLessStubDataset(openLineage, true), config(true));

    assertFalse(
        urn.isPresent(),
        "A schema-carrying REST-symlink dataset without a catalog facet must still be dropped");
  }

  @Test
  public void testStorageNamespaceSymlinkKeepsSymlinkResolution() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    // Hadoop/Hive-style catalogs symlink with a storage-path namespace, not a REST endpoint:
    // their bare name is not cross-catalog ambiguous, so default resolution is preserved.
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            facetLessStubDataset(openLineage, true, "s3a://bucket/warehouse"), config(true));

    assertTrue(urn.isPresent());
    assertEquals(
        urn.get().toString(),
        "urn:li:dataset:(urn:li:dataPlatform:hive,db.orders,PROD)",
        "Non-REST symlink namespaces keep default symlink resolution");
  }

  @Test
  public void testFacetLessStubKeptWhenFlagOff() {
    OpenLineage openLineage = new OpenLineage(URI.create(POLARIS_URI));
    // Without the option, behavior is unchanged: the stub resolves via symlinks as before.
    Optional<com.linkedin.common.urn.DatasetUrn> urn =
        OpenLineageToDataHub.convertOpenlineageDatasetToDatasetUrn(
            facetLessStubDataset(openLineage, false), config(false));

    assertTrue(urn.isPresent());
    assertEquals(
        urn.get().toString(),
        "urn:li:dataset:(urn:li:dataPlatform:hive,db.orders,PROD)",
        "Flag off keeps the pre-existing symlink resolution for facet-less datasets");
  }
}

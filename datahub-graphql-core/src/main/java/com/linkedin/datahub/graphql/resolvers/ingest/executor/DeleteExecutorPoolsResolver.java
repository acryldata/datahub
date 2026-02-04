package com.linkedin.datahub.graphql.resolvers.ingest.executor;

import com.linkedin.datahub.graphql.QueryContext;
import com.linkedin.datahub.graphql.concurrency.GraphQLConcurrencyUtils;
import com.linkedin.datahub.graphql.exception.AuthorizationException;
import com.linkedin.datahub.graphql.resolvers.ingest.IngestionAuthUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Deletes one or more remote executor pools by id. Requires the MANAGE ingestion privilege.
 *
 * <p>In OSS this is a no-op and returns the input pool ids. Managed DataHub may override or
 * replace this resolver to perform actual deletion against an external executor service.
 */
public class DeleteExecutorPoolsResolver implements DataFetcher<CompletableFuture<List<String>>> {

  @Override
  public CompletableFuture<List<String>> get(final DataFetchingEnvironment environment)
      throws Exception {

    final QueryContext context = environment.getContext();

    if (IngestionAuthUtils.canManageIngestion(context)) {
      @SuppressWarnings("unchecked")
      final List<String> poolIds = environment.getArgument("poolIds");
      return GraphQLConcurrencyUtils.supplyAsync(
          () -> ExecutorPoolStore.delete(poolIds != null ? poolIds : List.of()),
          this.getClass().getSimpleName(),
          "get");
    }
    throw new AuthorizationException(
        "Unauthorized to perform this action. Please contact your DataHub administrator.");
  }
}

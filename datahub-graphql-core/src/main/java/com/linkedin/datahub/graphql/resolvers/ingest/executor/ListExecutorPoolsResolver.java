package com.linkedin.datahub.graphql.resolvers.ingest.executor;

import static com.linkedin.datahub.graphql.resolvers.ResolverUtils.bindArgument;

import com.linkedin.datahub.graphql.QueryContext;
import com.linkedin.datahub.graphql.concurrency.GraphQLConcurrencyUtils;
import com.linkedin.datahub.graphql.exception.AuthorizationException;
import com.linkedin.datahub.graphql.generated.ExecutorPool;
import com.linkedin.datahub.graphql.generated.ListExecutorPoolsInput;
import com.linkedin.datahub.graphql.generated.ListExecutorPoolsResult;
import com.linkedin.datahub.graphql.resolvers.ingest.IngestionAuthUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Lists remote executor pools. Requires the MANAGE ingestion privilege.
 *
 * <p>In OSS this returns pools from an in-memory store (see ExecutorPoolStore). Managed DataHub
 * may override or replace this resolver to return pools from an external executor service.
 */
public class ListExecutorPoolsResolver implements DataFetcher<CompletableFuture<ListExecutorPoolsResult>> {

  private static final Integer DEFAULT_START = 0;
  private static final Integer DEFAULT_COUNT = 20;

  @Override
  public CompletableFuture<ListExecutorPoolsResult> get(final DataFetchingEnvironment environment)
      throws Exception {

    final QueryContext context = environment.getContext();

    if (IngestionAuthUtils.canManageIngestion(context)) {
      return GraphQLConcurrencyUtils.supplyAsync(
          () -> {
            final ListExecutorPoolsInput input =
                bindArgument(environment.getArgument("input"), ListExecutorPoolsInput.class);
            final int start = input.getStart() == null ? DEFAULT_START : input.getStart();
            final int count = input.getCount() == null ? DEFAULT_COUNT : input.getCount();

            int total = ExecutorPoolStore.totalCount();
            List<ExecutorPool> pools = ExecutorPoolStore.list(start, count);

            final ListExecutorPoolsResult result = new ListExecutorPoolsResult();
            result.setStart(start);
            result.setCount(count);
            result.setTotal(total);
            result.setPools(pools);
            return result;
          },
          this.getClass().getSimpleName(),
          "get");
    }
    throw new AuthorizationException(
        "Unauthorized to perform this action. Please contact your DataHub administrator.");
  }
}

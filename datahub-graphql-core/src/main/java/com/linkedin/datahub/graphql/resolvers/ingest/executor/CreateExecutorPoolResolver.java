package com.linkedin.datahub.graphql.resolvers.ingest.executor;

import static com.linkedin.datahub.graphql.resolvers.ResolverUtils.bindArgument;

import com.linkedin.datahub.graphql.QueryContext;
import com.linkedin.datahub.graphql.concurrency.GraphQLConcurrencyUtils;
import com.linkedin.datahub.graphql.exception.AuthorizationException;
import com.linkedin.datahub.graphql.generated.CreateExecutorPoolInput;
import com.linkedin.datahub.graphql.resolvers.ingest.IngestionAuthUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.concurrent.CompletableFuture;

/**
 * Creates a remote executor pool. Requires the MANAGE ingestion privilege.
 *
 * <p>In OSS this is a stub that returns the input pool id. Managed DataHub may override or replace
 * this resolver to perform actual creation against an external executor service.
 */
public class CreateExecutorPoolResolver implements DataFetcher<CompletableFuture<String>> {

  @Override
  public CompletableFuture<String> get(final DataFetchingEnvironment environment) throws Exception {

    final QueryContext context = environment.getContext();

    if (IngestionAuthUtils.canManageIngestion(context)) {
      return GraphQLConcurrencyUtils.supplyAsync(
          () -> {
            final CreateExecutorPoolInput input =
                bindArgument(environment.getArgument("input"), CreateExecutorPoolInput.class);
            String id = input.getId() != null ? input.getId() : "";
            String name = input.getName();
            return ExecutorPoolStore.create(id, name);
          },
          this.getClass().getSimpleName(),
          "get");
    }
    throw new AuthorizationException(
        "Unauthorized to perform this action. Please contact your DataHub administrator.");
  }
}

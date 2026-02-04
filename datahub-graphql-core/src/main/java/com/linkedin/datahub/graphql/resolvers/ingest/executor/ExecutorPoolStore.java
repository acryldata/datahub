package com.linkedin.datahub.graphql.resolvers.ingest.executor;

import com.linkedin.datahub.graphql.generated.ExecutorPool;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory store for executor pools. Used by OSS resolvers so create/list/delete work within a
 * single JVM. Does not persist across restarts. Managed DataHub may use external storage instead.
 */
final class ExecutorPoolStore {

  private static final Map<String, ExecutorPool> POOLS = new ConcurrentHashMap<>();

  static List<ExecutorPool> list(int start, int count) {
    List<ExecutorPool> all = new ArrayList<>(POOLS.values());
    int total = all.size();
    int from = Math.min(start, total);
    int to = Math.min(start + count, total);
    return new ArrayList<>(all.subList(from, to));
  }

  static int totalCount() {
    return POOLS.size();
  }

  static String create(String id, String name) {
    if (id == null || id.isBlank()) {
      return "";
    }
    ExecutorPool pool = new ExecutorPool();
    pool.setId(id.trim());
    pool.setName(name != null && !name.isBlank() ? name.trim() : null);
    POOLS.put(id.trim(), pool);
    return id.trim();
  }

  static List<String> delete(List<String> poolIds) {
    if (poolIds == null) {
      return List.of();
    }
    return poolIds.stream()
        .filter(Objects::nonNull)
        .filter(id -> POOLS.remove(id) != null)
        .collect(Collectors.toList());
  }

  private ExecutorPoolStore() {}
}

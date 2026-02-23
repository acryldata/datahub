import { message } from 'antd';
import React, { useCallback, useMemo } from 'react';

import { CreateExecutorPoolInput } from '@app/ingestV2/executor/CreateExecutorPoolModal';
import { ExecutorPool, ExecutorPoolsList } from '@app/ingestV2/executor/ExecutorPoolsList';

import {
    useCreateExecutorPoolMutation,
    useDeleteExecutorPoolsMutation,
    useListExecutorPoolsQuery,
    useListIngestionSourcesQuery,
} from '@graphql/ingestion.generated';

const FETCH_COUNT = 500;
const SOURCES_FETCH_COUNT = 1000;

export const ExecutorPoolsTab = () => {
    const { loading, error, data, refetch } = useListExecutorPoolsQuery({
        variables: {
            input: {
                start: 0,
                count: FETCH_COUNT,
            },
        },
    });

    const { data: sourcesData } = useListIngestionSourcesQuery({
        variables: {
            input: {
                start: 0,
                count: SOURCES_FETCH_COUNT,
                query: undefined,
            },
        },
    });

    const sourcesByExecutorId = useMemo(() => {
        const map: Record<string, string[]> = {};
        const sources = sourcesData?.listIngestionSources?.ingestionSources ?? [];
        sources.forEach((source) => {
            const executorId = source?.config?.executorId?.trim();
            if (executorId) {
                if (!map[executorId]) map[executorId] = [];
                map[executorId].push(source?.name ?? source?.urn ?? 'Unknown');
            }
        });
        return map;
    }, [sourcesData]);

    const [deleteExecutorPoolsMutation] = useDeleteExecutorPoolsMutation();
    const [createExecutorPoolMutation] = useCreateExecutorPoolMutation();

    const pools: ExecutorPool[] =
        data?.listExecutorPools?.pools?.map((p) => ({
            id: p.id,
            name: p.name ?? undefined,
        })) ?? [];

    const onDeletePools = useCallback(
        async (poolIds: string[]) => {
            await deleteExecutorPoolsMutation({
                variables: { poolIds },
            });
        },
        [deleteExecutorPoolsMutation],
    );

    const onCreatePool = useCallback(
        async (input: CreateExecutorPoolInput) => {
            try {
                await createExecutorPoolMutation({
                    variables: {
                        input: {
                            id: input.id,
                            name: input.name ?? null,
                        },
                    },
                });
                message.success({ content: 'Executor pool created.', duration: 2 });
                refetch?.();
            } catch (e) {
                message.destroy();
                message.error({
                    content: e instanceof Error ? e.message : 'Failed to create executor pool',
                    duration: 3,
                });
                throw e;
            }
        },
        [createExecutorPoolMutation, refetch],
    );

    return (
        <ExecutorPoolsList
            pools={pools}
            loading={loading}
            error={error ?? null}
            sourcesByExecutorId={sourcesByExecutorId}
            onDeletePools={onDeletePools}
            onCreatePool={onCreatePool}
            refetch={refetch}
        />
    );
};

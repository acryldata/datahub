import { MockedProvider } from '@apollo/client/testing';
import { render, screen } from '@testing-library/react';
import React from 'react';
import { ThemeProvider } from 'styled-components';
import { vi } from 'vitest';

import { ExecutorPoolsTab } from '@app/ingestV2/executor/ExecutorPoolsTab';
import themeV2 from '@conf/theme/themeV2';

import {
    useCreateExecutorPoolMutation,
    useDeleteExecutorPoolsMutation,
    useListExecutorPoolsQuery,
    useListIngestionSourcesQuery,
} from '@graphql/ingestion.generated';

vi.mock('@graphql/ingestion.generated', async (importOriginal) => {
    const actual = await importOriginal<typeof import('@graphql/ingestion.generated')>();
    return {
        ...actual,
        useListExecutorPoolsQuery: vi.fn(),
        useListIngestionSourcesQuery: vi.fn(),
        useDeleteExecutorPoolsMutation: vi.fn(),
        useCreateExecutorPoolMutation: vi.fn(),
    };
});

describe('ExecutorPoolsTab', () => {
    const mockRefetch = vi.fn();
    const mockDeleteMutation = vi.fn().mockResolvedValue({ data: {} });
    const mockCreateMutation = vi.fn().mockResolvedValue({ data: { createExecutorPool: 'new-id' } });

    beforeEach(() => {
        vi.clearAllMocks();
        (useListExecutorPoolsQuery as ReturnType<typeof vi.fn>).mockReturnValue({
            loading: false,
            error: null,
            data: {
                listExecutorPools: {
                    start: 0,
                    count: 500,
                    total: 0,
                    pools: [],
                },
            },
            refetch: mockRefetch,
        });
        (useListIngestionSourcesQuery as ReturnType<typeof vi.fn>).mockReturnValue({
            data: { listIngestionSources: { ingestionSources: [] } },
        });
        (useDeleteExecutorPoolsMutation as ReturnType<typeof vi.fn>).mockReturnValue([mockDeleteMutation]);
        (useCreateExecutorPoolMutation as ReturnType<typeof vi.fn>).mockReturnValue([mockCreateMutation]);
    });

    const renderTab = () =>
        render(
            <ThemeProvider theme={themeV2}>
                <MockedProvider mocks={[]} addTypename={false}>
                    <ExecutorPoolsTab />
                </MockedProvider>
            </ThemeProvider>,
        );

    it('renders ExecutorPoolsList with empty pools and Create pool button', () => {
        renderTab();
        expect(screen.getByText(/no executor pools yet/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /create pool/i })).toBeInTheDocument();
    });

    it('renders pools from listExecutorPools query', () => {
        (useListExecutorPoolsQuery as ReturnType<typeof vi.fn>).mockReturnValue({
            loading: false,
            error: null,
            data: {
                listExecutorPools: {
                    start: 0,
                    count: 500,
                    total: 1,
                    pools: [{ id: 'pool-1', name: 'Test Pool' }],
                },
            },
            refetch: mockRefetch,
        });

        renderTab();
        expect(screen.getByText('pool-1')).toBeInTheDocument();
        expect(screen.getByText('Test Pool')).toBeInTheDocument();
    });

    it('shows error state when list query returns error', () => {
        (useListExecutorPoolsQuery as ReturnType<typeof vi.fn>).mockReturnValue({
            loading: false,
            error: new Error('Network error'),
            data: undefined,
            refetch: mockRefetch,
        });

        renderTab();
        expect(screen.getByText('Failed to load executor pools')).toBeInTheDocument();
    });
});

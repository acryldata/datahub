import { Button, Pagination, Table, colors } from '@components';
import { Alert, Typography, message } from 'antd';
import React, { useCallback, useMemo, useState } from 'react';
import styled from 'styled-components';

import TabToolbar from '@app/entity/shared/components/styled/TabToolbar';
import EmptySources from '@app/ingestV2/EmptySources';
import { DEFAULT_PAGE_SIZE } from '@app/ingestV2/constants';
import { CreateExecutorPoolInput, CreateExecutorPoolModal } from '@app/ingestV2/executor/CreateExecutorPoolModal';
import { scrollToTop } from '@app/shared/searchUtils';
import { ConfirmationModal } from '@app/sharedV2/modals/ConfirmationModal';

const PoolsContainer = styled.div`
    display: flex;
    flex-direction: column;
    height: 100%;
    overflow: auto;
`;

const StyledTabToolbar = styled(TabToolbar)`
    padding: 0 20px 16px 0;
    height: auto;
    box-shadow: none;
    border-bottom: none;
`;

const ToolbarActions = styled.div`
    display: flex;
    align-items: center;
    gap: 8px;
`;

const TableContainer = styled.div`
    flex: 1;
    overflow: auto;
`;

const TextContainer = styled(Typography.Text)`
    color: ${colors.gray[1700]};
`;

export type ExecutorPool = {
    id: string;
    name?: string;
};

type TableRow = {
    id: string;
    name: string;
};

interface Props {
    pools: ExecutorPool[];
    loading?: boolean;
    error?: Error | null;
    sourcesByExecutorId?: Record<string, string[]>;
    onDeletePools: (poolIds: string[]) => Promise<void>;
    onCreatePool?: (input: CreateExecutorPoolInput) => Promise<void>;
    refetch?: () => void;
}

export const ExecutorPoolsList = ({
    pools,
    loading = false,
    error = null,
    sourcesByExecutorId = {},
    onDeletePools,
    onCreatePool,
    refetch,
}: Props) => {
    const [page, setPage] = useState(1);
    const [selectedPoolIds, setSelectedPoolIds] = useState<string[]>([]);
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
    const [isDeleting, setIsDeleting] = useState(false);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const poolsInUse = useMemo(
        () => selectedPoolIds.filter((id) => sourcesByExecutorId[id] && sourcesByExecutorId[id].length > 0),
        [selectedPoolIds, sourcesByExecutorId],
    );
    const deleteBlocked = poolsInUse.length > 0;
    const deleteConfirmContent = useMemo(() => {
        if (deleteBlocked) {
            const lines = poolsInUse.map(
                (id) =>
                    `Pool "${id}" is used by: ${sourcesByExecutorId[id].join(', ')}. Remove or reassign sources before deleting.`,
            );
            return lines.join('\n\n');
        }
        return `Are you sure you want to delete ${selectedPoolIds.length} selected pool${selectedPoolIds.length === 1 ? '' : 's'}? Ingestion sources using these pools may no longer run as expected.`;
    }, [deleteBlocked, poolsInUse, selectedPoolIds.length, sourcesByExecutorId]);

    const pageSize = DEFAULT_PAGE_SIZE;
    const start = (page - 1) * pageSize;
    const totalPools = pools.length;
    const paginatedPools = pools.slice(start, start + pageSize);

    const tableData: TableRow[] = paginatedPools.map((pool) => ({
        id: pool.id,
        name: pool.name ?? pool.id,
    }));

    const onChangePage = useCallback((newPage: number) => {
        scrollToTop();
        setPage(newPage);
    }, []);

    const handleSelectionChange = useCallback((selectedKeys: string[]) => {
        setSelectedPoolIds(selectedKeys);
    }, []);

    const handleDeleteClick = useCallback(() => {
        if (selectedPoolIds.length === 0) return;
        setShowDeleteConfirm(true);
    }, [selectedPoolIds.length]);

    const handleConfirmDelete = useCallback(async () => {
        if (selectedPoolIds.length === 0 || deleteBlocked) {
            if (deleteBlocked) setShowDeleteConfirm(false);
            return;
        }
        setIsDeleting(true);
        try {
            await onDeletePools(selectedPoolIds);
            message.success({
                content: `Deleted ${selectedPoolIds.length} pool${selectedPoolIds.length === 1 ? '' : 's'}.`,
                duration: 2,
            });
            setSelectedPoolIds([]);
            setShowDeleteConfirm(false);
            refetch?.();
        } catch (e) {
            message.destroy();
            if (e instanceof Error) {
                message.error({ content: `Failed to delete pool(s): ${e.message}`, duration: 3 });
            }
        } finally {
            setIsDeleting(false);
        }
    }, [deleteBlocked, onDeletePools, refetch, selectedPoolIds]);

    const handleCloseDeleteConfirm = useCallback(() => {
        if (!isDeleting) setShowDeleteConfirm(false);
    }, [isDeleting]);

    const columns = [
        {
            title: 'Pool ID',
            key: 'id',
            render: (record: TableRow) => (
                <TextContainer
                    ellipsis={{
                        tooltip: {
                            title: record.id,
                            color: 'white',
                            overlayInnerStyle: { color: colors.gray[1700] },
                            showArrow: false,
                        },
                    }}
                >
                    {record.id}
                </TextContainer>
            ),
            sorter: (a: TableRow, b: TableRow) => a.id.localeCompare(b.id),
        },
        {
            title: 'Name',
            key: 'name',
            render: (record: TableRow) => (
                <TextContainer
                    ellipsis={{
                        tooltip: {
                            title: record.name,
                            color: 'white',
                            overlayInnerStyle: { color: colors.gray[1700] },
                            showArrow: false,
                        },
                    }}
                >
                    {record.name}
                </TextContainer>
            ),
            sorter: (a: TableRow, b: TableRow) => a.name.localeCompare(b.name),
        },
    ];

    return (
        <>
            <PoolsContainer>
                <StyledTabToolbar>
                    <ToolbarActions>
                        {onCreatePool && (
                            <Button
                                variant="filled"
                                onClick={() => setShowCreateModal(true)}
                                icon={{ icon: 'Plus', source: 'phosphor' }}
                                data-testid="create-pool-button"
                            >
                                Create pool
                            </Button>
                        )}
                        <Button
                            variant="filled"
                            color="red"
                            disabled={selectedPoolIds.length === 0}
                            onClick={handleDeleteClick}
                            icon={{ icon: 'Trash', source: 'phosphor' }}
                            data-testid="delete-pool-button"
                        >
                            Delete Pool{selectedPoolIds.length !== 1 ? 's' : ''}
                        </Button>
                    </ToolbarActions>
                </StyledTabToolbar>
                {error && (
                    <Alert
                        type="error"
                        showIcon
                        style={{ marginBottom: 16 }}
                        message="Failed to load executor pools"
                        description={error.message}
                    />
                )}
                {!loading && totalPools === 0 && !error ? (
                    <EmptySources sourceType="executor pools" />
                ) : (
                    <>
                        <TableContainer>
                            <Table<TableRow>
                                columns={columns}
                                data={tableData}
                                rowKey="id"
                                isScrollable
                                style={{ tableLayout: 'fixed' }}
                                isLoading={loading}
                                rowSelection={{
                                    selectedRowKeys: selectedPoolIds,
                                    onChange: (selectedKeys) => handleSelectionChange(selectedKeys as string[]),
                                }}
                            />
                        </TableContainer>
                        <Pagination
                            currentPage={page}
                            itemsPerPage={pageSize}
                            total={totalPools}
                            showLessItems
                            onPageChange={onChangePage}
                            showSizeChanger={false}
                            hideOnSinglePage
                        />
                    </>
                )}
            </PoolsContainer>
            <ConfirmationModal
                isOpen={showDeleteConfirm}
                modalTitle={deleteBlocked ? 'Cannot delete pool(s)' : 'Delete executor pool(s)'}
                modalText={deleteConfirmContent}
                handleConfirm={deleteBlocked ? handleCloseDeleteConfirm : handleConfirmDelete}
                handleClose={handleCloseDeleteConfirm}
                isDeleteModal={!deleteBlocked}
                hideConfirmButton={deleteBlocked}
            />
            {onCreatePool && (
                <CreateExecutorPoolModal
                    open={showCreateModal}
                    onSubmit={onCreatePool}
                    onCancel={() => setShowCreateModal(false)}
                />
            )}
        </>
    );
};

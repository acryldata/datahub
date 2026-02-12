import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import React from 'react';
import { describe, expect, it, vi } from 'vitest';

import { CreateExecutorPoolModal } from '@app/ingestV2/executor/CreateExecutorPoolModal';

describe('CreateExecutorPoolModal', () => {
    const mockOnSubmit = vi.fn().mockResolvedValue(undefined);
    const mockOnCancel = vi.fn();

    it('renders when open with Pool ID and Name fields', () => {
        render(<CreateExecutorPoolModal open onSubmit={mockOnSubmit} onCancel={mockOnCancel} />);
        expect(screen.getByText('Create executor pool')).toBeInTheDocument();
        expect(screen.getByLabelText(/pool id/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/name \(optional\)/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /create/i })).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /cancel/i })).toBeInTheDocument();
    });

    it('does not render when closed', () => {
        render(<CreateExecutorPoolModal open={false} onSubmit={mockOnSubmit} onCancel={mockOnCancel} />);
        expect(screen.queryByText('Create executor pool')).not.toBeInTheDocument();
    });

    it('calls onCancel when Cancel clicked', () => {
        render(<CreateExecutorPoolModal open onSubmit={mockOnSubmit} onCancel={mockOnCancel} />);
        fireEvent.click(screen.getByRole('button', { name: /cancel/i }));
        expect(mockOnCancel).toHaveBeenCalledTimes(1);
    });

    it('calls onSubmit with id and optional name when Create clicked', async () => {
        render(<CreateExecutorPoolModal open onSubmit={mockOnSubmit} onCancel={mockOnCancel} />);
        fireEvent.change(screen.getByPlaceholderText(/e.g. my-pool/i), {
            target: { value: 'my-pool' },
        });
        fireEvent.change(screen.getByPlaceholderText(/display name/i), {
            target: { value: 'My Display Name' },
        });
        fireEvent.click(screen.getByRole('button', { name: /^create$/i }));

        await waitFor(() => {
            expect(mockOnSubmit).toHaveBeenCalledWith({
                id: 'my-pool',
                name: 'My Display Name',
            });
        });
    });

    it('requires Pool ID (validation)', async () => {
        mockOnSubmit.mockClear();
        render(<CreateExecutorPoolModal open onSubmit={mockOnSubmit} onCancel={mockOnCancel} />);
        // Leave Pool ID empty, click Create
        fireEvent.click(screen.getByRole('button', { name: /^create$/i }));
        await waitFor(() => {
            expect(screen.getByText(/required/i)).toBeInTheDocument();
        });
        expect(mockOnSubmit).not.toHaveBeenCalled();
    });
});

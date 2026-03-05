import { Form, Input } from 'antd';
import React, { useState } from 'react';

import { Modal } from '@src/alchemy-components';

const ID_FIELD = 'id';
const NAME_FIELD = 'name';

export type CreateExecutorPoolInput = {
    id: string;
    name?: string;
};

type Props = {
    open: boolean;
    onSubmit: (input: CreateExecutorPoolInput) => Promise<void>;
    onCancel: () => void;
};

export const CreateExecutorPoolModal = ({ open, onSubmit, onCancel }: Props) => {
    const [submitting, setSubmitting] = useState(false);
    const [form] = Form.useForm();

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            setSubmitting(true);
            await onSubmit({
                id: values[ID_FIELD]?.trim() ?? '',
                name: values[NAME_FIELD]?.trim() || undefined,
            });
            form.resetFields();
            onCancel();
        } catch (e) {
            if (e && typeof e === 'object' && 'errorFields' in e) return;
            throw e;
        } finally {
            setSubmitting(false);
        }
    };

    const handleClose = () => {
        if (!submitting) {
            form.resetFields();
            onCancel();
        }
    };

    return (
        <Modal
            width={480}
            title="Create executor pool"
            open={open}
            onCancel={handleClose}
            buttons={[
                { text: 'Cancel', variant: 'text', onClick: handleClose },
                {
                    text: submitting ? 'Creating…' : 'Create',
                    variant: 'filled',
                    disabled: submitting,
                    buttonDataTestId: 'create-executor-pool-button',
                    onClick: handleSubmit,
                },
            ]}
        >
            <Form form={form} layout="vertical" preserve={false}>
                <Form.Item
                    name={ID_FIELD}
                    label="Pool ID"
                    rules={[
                        { required: true, message: 'Pool ID is required' },
                        { whitespace: true, message: 'Pool ID cannot be blank' },
                    ]}
                >
                    <Input placeholder="e.g. my-pool" data-testid="create-pool-id-input" />
                </Form.Item>
                <Form.Item name={NAME_FIELD} label="Name (optional)">
                    <Input placeholder="Display name" data-testid="create-pool-name-input" />
                </Form.Item>
            </Form>
        </Modal>
    );
};

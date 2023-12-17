import { Button, Select, Stack, TextInput, Textarea } from '@mantine/core';
import { useForm } from '@mantine/form';
import { zodResolver } from 'mantine-form-zod-resolver';
import * as z from 'zod';
import { CreateIssuePayload, Issue } from '../store/issuesApiSlice';
import { User } from '@/store/userApiSlice';


export type CreateIssueFromProps = {
    isLoading: boolean;
    projectCode: string;
    epics: Issue[];
    users: User[];
    handleCreateIssue: (values: CreateIssuePayload) => void;
};

const ISSUE_TYPES = [
    "EPIC",
    "DEFECT",
    "STORY",
    "TASK",
    "SPIKE",
] as const;
const ISSUE_PRIORITIES = ["LOW", "MEDIUM", "HIGH", "BLOCKER"] as const;

const schema = z
    .object({
        summary: z.string().min(1),
        description: z.string().min(1),
        story_points: z.coerce.number().min(0.5),
        type: z.enum(ISSUE_TYPES),
        priority: z.enum(ISSUE_PRIORITIES),
        epic_code: z.string(),
        assignee_email: z.string().email(),
    })
    .refine(
        (data) =>
            (data.type == "EPIC" && data.epic_code == "") ||
            (data.type != "EPIC" && data.epic_code != ""),
        {
            message: "EPIC is required",
            path: ["epic_code"],
        }
    );

export function CreateIssueForm({ isLoading, handleCreateIssue, epics, users, projectCode }: CreateIssueFromProps) {
    const form = useForm({
        initialValues: {
            summary: "",
            description: "",
            type: "STORY",
            priority: "LOW",
            story_points: 0.5,
            epicCode: "",
            assigneeEmail: "",
        },
        validate: zodResolver(schema),
    });

    async function onSubmit() {
        handleCreateIssue({ ...form.values, projectCode });
        form.reset();
    }

    return (
        <form onSubmit={form.onSubmit(() => onSubmit())}>
            <Stack gap="lg">
                <TextInput
                    withAsterisk
                    label="Summary"
                    placeholder="Summary of your issue"
                    {...form.getInputProps('summary')}
                />

                <Textarea
                    withAsterisk
                    label="Description"
                    rows={3}
                    placeholder="Describe your issue"
                    {...form.getInputProps('description')}
                />

                <TextInput
                    withAsterisk
                    label="Estimated Story Points"
                    placeholder="Estimated Story Points"
                    type='number'
                    {...form.getInputProps('story_points')}
                />


                <Select
                    withAsterisk
                    label="Epic"
                    placeholder='Choose EPIC for your issue'
                    searchable
                    data={epics.map(e => ({ value: e.code, label: `${e.code} - ${e.summary}` }))}
                    {...form.getInputProps('epic_code')}
                />

                <Select
                    withAsterisk
                    label="Type"
                    placeholder='Type of your issue'
                    searchable
                    data={[...ISSUE_TYPES]}
                    {...form.getInputProps('type')}
                />

                <Select
                    withAsterisk
                    label="Priority"
                    placeholder='Priority of your issue'
                    searchable
                    data={[...ISSUE_PRIORITIES]}
                    {...form.getInputProps('priority')}
                />

                <Select
                    withAsterisk
                    label="Assignee"
                    placeholder='Assignee your issue to a user'
                    data={users.map(u => ({ value: u.email, label: `${u.email} - ${u.display_name}` }))}
                    {...form.getInputProps('assignee_email')}
                />

                <Button type="submit" fullWidth loading={isLoading}>
                    Create Issue
                </Button>
            </Stack>
        </form>
    );
}

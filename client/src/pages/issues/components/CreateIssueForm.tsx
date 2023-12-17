import { Button, Stack, TextInput } from '@mantine/core';
import { useForm } from '@mantine/form';
import { zodResolver } from 'mantine-form-zod-resolver';
import * as z from 'zod';
import { CreateIssuePayload, IssueType, IssuePriority } from '../store/issuesApiSlice';


export type CreateIssueFromProps = {
    isLoading: boolean;
    projectCode: string;
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
        epicCode: z.string(),
        assigneeEmail: z.string().email(),
    })
    .refine(
        (data) =>
            (data.type == "EPIC" && data.epicCode == "") ||
            (data.type != "EPIC" && data.epicCode != ""),
        {
            message: "EPIC is required",
            path: ["epicCode"],
        }
    );

export function CreateIssueForm({ isLoading, handleCreateIssue, projectCode }: CreateIssueFromProps) {
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

                <TextInput
                    withAsterisk
                    label="Description"
                    placeholder="Describe your issue"
                    {...form.getInputProps('summary')}
                />

                <Button type="submit" fullWidth loading={isLoading}>
                    Create Issue
                </Button>
            </Stack>
        </form>
    );
}

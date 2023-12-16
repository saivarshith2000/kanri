import { Button, Stack, TextInput } from "@mantine/core";
import { useForm } from "@mantine/form";
import { zodResolver } from "mantine-form-zod-resolver";
import * as z from "zod";
import { CreateProjectPayload } from "../store";

export type CreateProjectFromProps = {
    isLoading: boolean,
    handleCreateProject: (values: CreateProjectPayload) => void
}

const schema = z.object({
    name: z.string().min(3).max(64),
    code: z.string().min(3).max(8),
    description: z.string().min(3).max(256),
});

export function CreateProjectForm({ isLoading, handleCreateProject }: CreateProjectFromProps) {
    const form = useForm({
        initialValues: {
            name: '',
            code: '',
            description: '',
        },
        validate: zodResolver(schema),
    });

    async function onSubmit() {
        handleCreateProject(form.values);
        form.reset();
    }

    return (
        <form onSubmit={form.onSubmit(() => onSubmit())}>
            <Stack gap="lg">
                <TextInput
                    withAsterisk
                    label="Name"
                    placeholder="Name your project"
                    {...form.getInputProps('name')}
                />

                <TextInput
                    withAsterisk
                    label="Code"
                    placeholder="Choose a unique code for your project"
                    {...form.getInputProps('code')}
                />

                <TextInput
                    withAsterisk
                    label="Description"
                    placeholder="Briefly describe your project"
                    {...form.getInputProps('description')}
                />

                <Button type="submit" fullWidth loading={isLoading}>
                    Create Project
                </Button>
            </Stack>
        </form>
    );
}
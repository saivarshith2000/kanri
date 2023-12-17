import { useDisclosure } from "@mantine/hooks";
import { CreateIssuePayload, useCreateIssueMutation } from "../store/issuesApiSlice";
import { notifications } from "@mantine/notifications";
import { isApiError } from "@/store/apiError";
import { Button, Modal } from "@mantine/core";
import { CreateIssueForm } from "./CreateIssueForm";

export type CreateIssueModalType = {
    projectCode: string;
};

export function CreateIssueModal({ projectCode }: CreateIssueModalType) {
    const [opened, { open, close }] = useDisclosure(false);
    const [createIssue, { isLoading }] = useCreateIssueMutation();

    async function handleCreateIssue(values: CreateIssuePayload) {
        try {
            await createIssue(values).unwrap();
            notifications.show({
                message: 'Issue <ISSUE-CODE> created successfully',     // todo: replace issue code here
                color: 'green',
                autoClose: 2000,
            });
            close();
        } catch (err) {
            let toastMsg = 'An error occured while creating new issue. Please try again later.';
            if (isApiError(err)) {
                toastMsg = err.data.errors.msg;
            }
            notifications.show({
                message: toastMsg,
                color: 'red',
                withCloseButton: true,
            });
        }
    }

    return (
        <>
            <Modal opened={opened} onClose={close} title="Create Issue" centered>
                <CreateIssueForm isLoading={isLoading} handleCreateIssue={handleCreateIssue} projectCode={projectCode} />
            </Modal>
            <Button onClick={open} variant="filled" size="sm">
                New Issue
            </Button>
        </>
    );
}
import { useDisclosure } from "@mantine/hooks";
import { CreateIssuePayload, useCreateIssueMutation, useGetEpicsQuery } from "../store/issuesApiSlice";
import { notifications } from "@mantine/notifications";
import { isApiError } from "@/store/apiError";
import { Button, Loader, Modal } from "@mantine/core";
import { CreateIssueForm } from "./CreateIssueForm";
import { useGetUsersQuery } from "@/store/userApiSlice";
import ErrorPage from "@/pages/ErrorPage/ErrorPage";

export type CreateIssueModalType = {
    projectCode: string;
};

export function CreateIssueModal({ projectCode }: CreateIssueModalType) {
    const [opened, { open, close }] = useDisclosure(false);
    const [createIssue, { isLoading }] = useCreateIssueMutation();
    const { data: users, isLoading: isUsersLoading, error: usersError } = useGetUsersQuery(projectCode);
    const {
        data: epics,
        isLoading: isEpicsLoading,
        error: epicsError,
    } = useGetEpicsQuery(projectCode);

    async function handleCreateIssue(values: CreateIssuePayload) {
        try {
            const { code } = await createIssue(values).unwrap();
            notifications.show({
                message: `Issue ${code} created successfully`,
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

    if (isUsersLoading || isEpicsLoading) {
        return <Loader />
    }

    if (users === undefined || epics === undefined) {
        notifications.show({
            message: "An error occured while fetching project details. Please try again later.",
            color: "red",
            withCloseButton: true
        })
        return <ErrorPage />
    }

    return (
        <>
            <Modal opened={opened} onClose={close} title="Create Issue" centered size="50%">
                <CreateIssueForm
                    isLoading={isLoading}
                    handleCreateIssue={handleCreateIssue}
                    projectCode={projectCode}
                    users={users}
                    epics={epics}
                />
            </Modal>
            <Button onClick={open} variant="filled" size="sm">
                New Issue
            </Button>
        </>
    );
}
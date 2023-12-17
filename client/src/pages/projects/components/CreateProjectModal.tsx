import { useDisclosure } from '@mantine/hooks';
import { Modal, Button } from '@mantine/core';
import { CreateProjectForm } from './CreateProjectForm';
import { CreateProjectPayload, useCreateProjectMutation } from '../store';
import { notifications } from '@mantine/notifications';
import { isApiError } from '@/store/apiError';

export type CreateProjectModalType = {
  isDisabled: boolean;
};

export function CreateProjectModal({ isDisabled }: CreateProjectModalType) {
  const [opened, { open, close }] = useDisclosure(false);
  const [createProject, { isLoading }] = useCreateProjectMutation();

  async function handleCreateProject(values: CreateProjectPayload) {
    try {
      await createProject(values).unwrap();
      notifications.show({
        message: 'Project created successfully',
        color: 'green',
        autoClose: 2000,
      });
      close();
    } catch (err) {
      let toastMsg = 'An error occured while creating a new project. Please try again later.';
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
      <Modal opened={opened} onClose={close} title="Create Project" centered>
        <CreateProjectForm isLoading={isLoading} handleCreateProject={handleCreateProject} />
      </Modal>
      <Button onClick={open} variant="outline" disabled={isDisabled}>
        New Project
      </Button>
    </>
  );
}

import { SimpleGrid, Loader, Text, Stack, Button, Group, Center } from '@mantine/core';
import { useGetProjectsQuery } from '../store';
import { notifications } from '@mantine/notifications';
import { ProjectCard } from './ProjectCard';
import { CreateProjectModal } from './CreateProjectModal';

export function Projects() {
  const { data: projects, error, isLoading } = useGetProjectsQuery();

  if (isLoading) {
    return <Loader />;
  } else if (error || projects === undefined) {
    notifications.show({
      message: 'An error occured while fetching your projects',
      color: 'red',
      autoClose: 2000,
    });
  } else if (projects.length === 0) {
    return (
      <Center>
        <Text>You Don't have any projects yet. Start by creating a new project</Text>
        <CreateProjectModal isDisabled={true} />
      </Center>
    );
  } else {
    const maxProjectLimit = parseInt(import.meta.env.VITE_KANRI_MAX_PROJECT_LIMIT);
    return (
      <Stack mt={32} w="50%" m="auto">
        <Group justify="space-between">
          <Text size="32px" fw="bold">
            Your Projects
          </Text>
          <CreateProjectModal isDisabled={projects.length >= maxProjectLimit} />
        </Group>
        <SimpleGrid cols={3} spacing={3}>
          {projects?.map((p) => <ProjectCard key={p.code} project={p} />)}
        </SimpleGrid>
      </Stack>
    );
  }
}

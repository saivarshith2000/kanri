import { Paper, Stack, Text } from '@mantine/core';
import { Project } from '../store';
import { ProjectRoleBadge } from './ProjectRoleBadge';
import { useNavigate } from 'react-router-dom';

type ProjectCardProps = {
  project: Project;
};

export function ProjectCard({ project }: ProjectCardProps) {
  const navigate = useNavigate();

  return (
    <Paper
      radius="md"
      p={16}
      w={250}
      withBorder
      mah="150px"
      onClick={() => navigate(`/projects/${project.code}/issues`)}
      style={{ cursor: 'pointer' }}
    >
      <Stack gap={2}>
        <Text fw="bold" size="lg">
          {project.code}
        </Text>
        <Text>{project.name}</Text>
        <ProjectRoleBadge role={project.role} />
      </Stack>
    </Paper>
  );
}

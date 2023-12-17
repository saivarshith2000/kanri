import { Badge } from '@mantine/core';
import { ProjectRole } from '../store';

type ProjectRoleBadgeProps = {
  role: ProjectRole;
};

const roleMap = {
  OWNER: 'red',
  ADMIN: 'yellow',
  USER: 'blue',
};

export function ProjectRoleBadge({ role }: ProjectRoleBadgeProps) {
  return (
    <Badge variant="filled" color={roleMap[role]} size="sm" fw="bold">
      {role}
    </Badge>
  );
}

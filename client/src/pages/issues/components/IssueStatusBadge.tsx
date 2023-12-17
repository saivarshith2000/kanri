import { Badge } from '@mantine/core';
import { IssueStatus } from '../store/issuesApiSlice';

const colorMap = {
  OPEN: 'blue',
  INPROGRESS: 'orange',
  CLOSED: 'green',
  REJECTED: 'gray',
};

export function IssueStatusBadge({ status }: { status: IssueStatus }) {
  return (
    <Badge variant="dot" color={colorMap[status]}>
      {status}
    </Badge>
  );
}

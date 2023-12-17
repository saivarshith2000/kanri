import { Group, Paper, Text } from '@mantine/core';
import { Issue } from '../store/issuesApiSlice';
import { IssuePriorityBadge } from './IssuePriorityBadge';
import { IssueStatusBadge } from './IssueStatusBadge';
import { IssueTypeBadge } from './IssueTypeBadge';
import { StoryPointsBadge } from './StoryPointsBadge';

export type IssueListItemProps = {
  issue: Issue;
};

export function IssueListItem({ issue }: IssueListItemProps) {
  return (
    <Paper px={8} py={4} withBorder>
      <Group justify="space-between">
        <Group>
          <IssueTypeBadge type={issue.type} />
          <Text fw="bold" size="sm">{issue.code}</Text>
          <Text size="sm">{issue.summary}</Text>
        </Group>
        <Group>
          <IssueStatusBadge status={issue.status} />
          <IssuePriorityBadge priority={issue.priority} />
          <StoryPointsBadge story_points={issue.story_points} />
        </Group>
      </Group>
    </Paper>
  );
}

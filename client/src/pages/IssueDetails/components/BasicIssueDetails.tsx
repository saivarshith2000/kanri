import { IssuePriorityBadge } from '@/pages/issues/components/IssuePriorityBadge';
import { IssueStatusBadge } from '@/pages/issues/components/IssueStatusBadge';
import { IssueTypeBadge } from '@/pages/issues/components/IssueTypeBadge';
import { StoryPointsBadge } from '@/pages/issues/components/StoryPointsBadge';
import { Issue } from '@/pages/issues/store/issuesApiSlice';
import { Group, Paper, SimpleGrid, Space, Stack, Text } from '@mantine/core';
import React from 'react';

export type BasicIssueDetailsProps = {
    issue: Issue;
}

export function Detail({ property, value }: { property: string, value: React.ReactNode }) {
    return <Group justify='space-between'>
        <Text fw="600" size='sm'>{property}</Text>
        {value}
    </Group>
}

export function BasicIssueDetails({ issue }: BasicIssueDetailsProps) {
    return <Paper radius='md' withBorder p={16} w='100%'>
        <Stack gap={16}>
            <Group>
                <Text fw="900" size='24px' c='blue'>{issue.code}</Text>
                <Text fw="900" size='24px' >{issue.summary}</Text>
            </Group>
            <SimpleGrid cols={{ sm: 1, md: 2 }} verticalSpacing={8} spacing={200}>
                <Detail property='Type' value={<IssueTypeBadge type={issue.type} />} />
                <Detail property='Reporter' value={<Text size="sm">{issue.reporter_email}</Text>} />
                <Detail property='Priority' value={<IssuePriorityBadge priority={issue.priority} />} />
                <Detail property='Assignee' value={<Text size="sm">{issue.assignee_email}</Text>} />
                <Detail property='Status' value={<IssueStatusBadge status={issue.status} />} />
                <Detail property='Created At' value={<Text size="sm">{issue.created_at}</Text>} />
                <Detail property='Estimated Story Points' value={<StoryPointsBadge story_points={issue.story_points} />} />
                <Detail property='Last Updated At' value={<Text size="sm">{issue.updated_at}</Text>} />
            </SimpleGrid>
        </Stack>
    </Paper>;
}
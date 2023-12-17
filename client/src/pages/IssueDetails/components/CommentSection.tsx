import { Button, Group, Paper, Stack, Text } from '@mantine/core';
import { IconPlus } from '@tabler/icons-react';

export function CommentSection() {
  return <Paper radius='md' withBorder p={16} w='100%'>
    <Stack>
      <Group justify='space-between'>
        <Text fw="900">Comments</Text>
        <Button rightSection={<IconPlus size='16px' />} size='xs' variant='light'>Comment</Button>
      </Group>
    </Stack>
    This is the comment section of this issue
  </Paper>;
}

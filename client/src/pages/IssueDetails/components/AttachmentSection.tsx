import { Button, Group, Paper, Stack, Text } from '@mantine/core';
import { IconPlus } from '@tabler/icons-react';

export function AttachmentSection() {
  return <Paper radius='md' withBorder p={16} w='100%'>
    <Stack>
      <Group justify='space-between'>
        <Text fw="900">Worklogs</Text>
        <Button rightSection={<IconPlus size='16px' />} size='xs' variant='light'>Attach</Button>
      </Group>
    </Stack>

    This is the attachment section of this issue
  </Paper>;
}

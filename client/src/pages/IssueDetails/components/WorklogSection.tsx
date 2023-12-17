import { Button, Group, Paper, Stack, Text } from '@mantine/core';
import { IconPlus } from '@tabler/icons-react';

export function WorklogDetails() {
    return <Paper radius='md' withBorder p={16} w='100%'>
        <Stack>
            <Group justify='space-between'>
                <Text fw="900">Worklogs</Text>
                <Button rightSection={<IconPlus size='16px' />} size='xs' variant='light'>Log Work</Button>
            </Group>
        </Stack>

        This is the worklog section of this issue
    </Paper>;
}

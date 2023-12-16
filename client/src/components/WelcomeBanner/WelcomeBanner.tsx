import { Title, Text, Anchor, Group, Button, Center, Stack } from '@mantine/core';
import classes from './WelcomeBanner.module.css';
import { Link } from 'react-router-dom';
import { IconArrowRight } from '@tabler/icons-react';

export function WelcomeBanner() {
  return (
    <Center>
      <Stack justify="center" mt={200} align="center">
        <Title className={classes.title} ta="center">
          <Text
            inherit
            variant="gradient"
            component="span"
            gradient={{ from: 'green', to: 'yellow' }}
          >
            K A N R I
          </Text>
        </Title>
        <Text c="dimmed" ta="center" size="lg" maw={580}>
          Project management made easy
        </Text>
        <Link to="/signup">
          <Button variant="subtle" size="compact-xl" mt={16} rightSection={<IconArrowRight />}>
            Try now
          </Button>
        </Link>
      </Stack>
    </Center>
  );
}

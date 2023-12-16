import { Flex, Group, Text, AppShell, Anchor } from '@mantine/core';
import { Outlet } from 'react-router';
import classes from './Root.module.css';
import { Link } from 'react-router-dom';
import { ColorSchemeToggle } from '../ColorSchemeToggle/ColorSchemeToggle';
import { isAuthenticatedSelector } from '@/pages/signin/store';
import { useSelector } from 'react-redux';

export function Root() {
  const isAuthenticated = useSelector(isAuthenticatedSelector);

  function renderMenuSection() {
    if (isAuthenticated) {
      return <Text>You are in!</Text>;
    }
    return (
      <>
        <Link to="/signin" style={{ textDecoration: 'none' }}>
          <Anchor fw="bold">Sign In</Anchor>
        </Link>
        <Link to="/signup" style={{ textDecoration: 'none' }}>
          <Anchor fw="bold">Sign Up</Anchor>
        </Link>
      </>
    );
  }

  return (
    <AppShell header={{ height: '80px' }}>
      <AppShell.Header>
        <Flex justify="space-between" w={{ base: '100%', md: '75%' }} m="auto">
          <Link to="/" style={{ textDecoration: 'none' }}>
            <Text
              variant="gradient"
              component="span"
              gradient={{ from: 'green', to: 'yellow' }}
              className={classes.logo}
            >
              KANRI
            </Text>
          </Link>
          <Group>
            {renderMenuSection()}
            <ColorSchemeToggle />
          </Group>
        </Flex>
      </AppShell.Header>
      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell >
  );
}

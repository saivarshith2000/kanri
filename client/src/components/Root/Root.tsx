import { Flex, Group, Text, AppShell, Anchor, Menu, Button } from '@mantine/core';
import { Outlet } from 'react-router';
import classes from './Root.module.css';
import { Link } from 'react-router-dom';
import { ColorSchemeToggle } from '../ColorSchemeToggle/ColorSchemeToggle';
import { authSelector, isAuthenticatedSelector, signout } from '@/pages/signin/store';
import { apiSlice } from "@/store/apiSlice";
import { useDispatch, useSelector } from 'react-redux';
import { FirebaseSignOutUser } from '@/firebase';

export function Root() {
  const isAuthenticated = useSelector(isAuthenticatedSelector);
  const auth = useSelector(authSelector);
  const dispatch = useDispatch();

  function handleSignout() {
    FirebaseSignOutUser();
    dispatch(signout());
    dispatch(apiSlice.util.resetApiState())
  }

  function renderMenuSection() {
    if (isAuthenticated) {
      return <Menu>
        <Menu.Target>
          <Button variant="subtle">{auth.name}</Button>
        </Menu.Target>
        <Menu.Dropdown>
          <Menu.Item>My Projects</Menu.Item>
          <Menu.Item>My Issues</Menu.Item>
          <Menu.Item onClick={handleSignout}>Sign out</Menu.Item>
        </Menu.Dropdown>
      </Menu>
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

import { Text, Paper } from '@mantine/core';
import { Link } from 'react-router-dom';
import { SignupForm } from './components/SignupForm';

export function Signup() {
  function handleSignup({
    name,
    email,
    password,
  }: {
    name: string;
    email: string;
    password: string;
  }) {
    console.log(name);
    console.log(email);
    console.log(password);
  }

  return (
    <>
      <Paper mx="auto" shadow="xs" radius="md" maw={{ sm: '80%', md: '40%' }} p="xl" mt="100px">
        <Text size="32px" fw="bold" ta="center">
          Sign Up
        </Text>
        <SignupForm handleSignup={handleSignup} />
      </Paper>
      <Link to="/signin" style={{ textDecoration: 'none' }}>
        <Text c="blue" ta="center" mt={16} style={{ textDecoration: 'underline' }}>
          Sign In using an existing account
        </Text>
      </Link>
    </>
  );
}

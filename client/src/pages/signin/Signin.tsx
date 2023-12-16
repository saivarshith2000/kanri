import { Text, Paper } from '@mantine/core';
import { Link, useNavigate } from 'react-router-dom';
import { SigninForm } from './components/SigninForm';
import { firebaseSignin } from '@/firebase';
import { useDispatch } from 'react-redux';
import { notifications } from '@mantine/notifications';
import { signin } from './store';
import { useState } from 'react';

export function Signin() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);

  async function handleSignin({ email, password }: { email: string; password: string }) {
    setIsLoading(true);
    const result = await firebaseSignin(email, password);
    setIsLoading(false);
    if (result.user) {
      notifications.show({
        message: 'Sign In Successful',
        autoClose: 2000,
        color: 'green',
      });
      dispatch(signin(result.user));
      navigate('/projects/');
    } else {
      notifications.show({
        title: 'Sign In Failed',
        message: result.error,
        autoClose: 2000,
        color: 'red',
      });
    }
  }

  return (
    <>
      <Paper mx="auto" shadow="xs" radius="md" maw={{ sm: '80%', md: '40%' }} p="xl" mt="100px">
        <Text size="32px" fw="bold" ta="center">
          Sign In
        </Text>
        <SigninForm handleSignin={handleSignin} isLoading={isLoading} />
      </Paper>
      <Link to="/signup" style={{ textDecoration: 'none' }}>
        <Text c="blue" ta="center" mt={16} style={{ textDecoration: 'underline' }}>
          Create an account
        </Text>
      </Link>
    </>
  );
}

import { Text, Paper } from '@mantine/core';
import { Link, useNavigate } from 'react-router-dom';
import { SignupForm } from './components/SignupForm';
import { useSignupMutation } from './store';
import { notifications } from '@mantine/notifications';
import { isApiError } from '@/store/apiError';

export function Signup() {
  const [signup, { isLoading }] = useSignupMutation();
  const navigate = useNavigate();

  async function handleSignup({
    name,
    email,
    password,
  }: {
    name: string;
    email: string;
    password: string;
  }) {
    try {
      await signup({ display_name: name, email, password }).unwrap();
      notifications.show({
        message: 'Sign up successful',
        color: 'green',
        autoClose: 2000,
      });
      navigate('/signin');
    } catch (err) {
      let toastMsg = 'An error occured while creating a new project. Please try again later';
      if (isApiError(err) && err.data != null) {
        toastMsg = err.data.errors.msg;
      }
      notifications.show({
        message: toastMsg,
        color: 'red',
        withCloseButton: true,
      });
    }
  }

  return (
    <>
      <Paper mx="auto" shadow="xs" radius="md" maw={{ sm: '80%', md: '30%' }} p="xl" mt={{ sm: "10px", md: "100px" }}>
        <Text size="32px" fw="bold" ta="center">
          Sign Up
        </Text>
        <SignupForm handleSignup={handleSignup} isLoading={isLoading} />
      </Paper>
      <Link to="/signin" style={{ textDecoration: 'none' }}>
        <Text c="blue" ta="center" mt={16} style={{ textDecoration: 'underline' }}>
          Sign In using an existing account
        </Text>
      </Link>
    </>
  );
}

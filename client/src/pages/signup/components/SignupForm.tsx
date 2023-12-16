import { TextInput, Button, Stack } from '@mantine/core';
import { useForm } from '@mantine/form';
import { zodResolver } from 'mantine-form-zod-resolver';
import { z } from 'zod';

const schema = z
  .object({
    name: z
      .string()
      .min(4, { message: 'Name should have at least 4 letters' })
      .max(16, 'Name should be within 16 letters'),
    email: z.string().email({ message: 'Invalid email' }),
    password: z
      .string()
      .min(4, { message: 'Password should have more than 4 letters' })
      .max(32, 'Password should be within 32 letters'),
    confirm_password: z.string(),
  })
  .refine((values) => values.password === values.confirm_password, {
    message: "Passwords don't match",
    path: ['confirm_password'],
  });

export type SignupFormProps = {
  isLoading: boolean;
  handleSignup: ({
    name,
    email,
    password,
  }: {
    name: string;
    email: string;
    password: string;
  }) => void;
};

export function SignupForm({ handleSignup, isLoading }: SignupFormProps) {
  const form = useForm({
    initialValues: {
      name: '',
      email: '',
      password: '',
      confirm_password: '',
    },
    validate: zodResolver(schema),
  });

  function handleSubmit() {
    handleSignup(form.values);
    form.reset();
  }

  return (
    <form onSubmit={form.onSubmit(() => handleSubmit())}>
      <Stack gap="lg">
        <TextInput
          withAsterisk
          label="Name"
          placeholder="Your name"
          {...form.getInputProps('name')}
        />

        <TextInput
          withAsterisk
          label="Email"
          placeholder="your@email.com"
          {...form.getInputProps('email')}
        />

        <TextInput
          withAsterisk
          label="Password"
          placeholder="Your password"
          type="password"
          {...form.getInputProps('password')}
        />

        <TextInput
          withAsterisk
          label="Confirm Password"
          placeholder="Enter your password again"
          type="password"
          {...form.getInputProps('confirm_password')}
        />

        <Button type="submit" fullWidth bg="green" loading={isLoading}>
          Sign Up
        </Button>
      </Stack>
    </form>
  );
}

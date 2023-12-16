import { TextInput, Button, Stack } from '@mantine/core';
import { useForm } from '@mantine/form';
import { zodResolver } from 'mantine-form-zod-resolver';
import { z } from 'zod';

export type SigninFormProps = {
  isLoading: boolean;
  handleSignin: ({ email, password }: { email: string; password: string }) => void;
};

const schema = z.object({
  email: z.string().email({ message: 'Invalid email' }),
  password: z
    .string()
    .min(4, { message: 'Password should have more than 4 letters' })
    .max(32, 'Password should be within 32 letters'),
});

export function SigninForm({ handleSignin, isLoading }: SigninFormProps) {
  const form = useForm({
    initialValues: {
      email: '',
      password: '',
    },
    validate: zodResolver(schema),
  });

  function onSubmit() {
    handleSignin(form.values);
    form.reset();
  }

  return (
    <form onSubmit={form.onSubmit(() => onSubmit())}>
      <Stack gap="lg">
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

        <Button type="submit" fullWidth loading={isLoading}>
          Submit
        </Button>
      </Stack>
    </form>
  );
}

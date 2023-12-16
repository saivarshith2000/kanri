import { Stack, Text } from "@mantine/core";
import { Link } from "react-router-dom";

export default function ErrorPage() {
  return (
    <Stack gap={16} justify="center" mt={48} mx="auto" ta="center">
      <Text size="48px">Uh oh!</Text>
      <Text c="dimmed" size="24px">You found a page doesn't exist</Text>
      <Link to="/" style={{ textDecoration: 'none' }}>
        <Text c="blue" fw="bold">Go back</Text>
      </Link>
    </Stack>
  );
}

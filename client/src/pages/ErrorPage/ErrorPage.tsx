import { Center, Text } from "@mantine/core";
import { Link } from "react-router-dom";

export default function ErrorPage() {
  return (
    <Center>
      <Text size="xl">Oops!</Text>
      <Link to="/">
        Go back
      </Link>
    </Center>
  );
}

import { Loader, Stack, Text } from "@mantine/core";
import { useParams } from "react-router";
import ErrorPage from "../ErrorPage/ErrorPage";
import { useGetIssuesQuery } from "./store/issuesApiSlice";
import { notifications } from "@mantine/notifications";
import { IssueListItem } from "./components/IssueListItem";

export function Issues() {
    const { projectCode } = useParams();
    if (projectCode === undefined) {
        return <ErrorPage />;
    }
    const { data: issues, error, isLoading } = useGetIssuesQuery(projectCode);

    if (isLoading) {
        return <Loader />;
    } else if (error || issues === undefined) {
        notifications.show({
            message: "An error occured while fetching issues",
            color: "red",
            withCloseButton: true
        })
    } else if (issues!.length === 0) {
        return <Text size="32px" c="dimmed">
            You don't have any issues yet. <br />
            Start by creating an EPIC
        </Text>
    }
    else {
        return <Stack w="75%" m="auto" mt={4} gap={0}>
            {issues!.map(i => <IssueListItem issue={i} key={i.code} />)}
        </Stack>
    }
}
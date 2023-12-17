import { Group, Loader, Stack, Text } from '@mantine/core';
import { useParams } from 'react-router';
import ErrorPage from '../ErrorPage/ErrorPage';
import { useGetIssuesQuery } from './store/issuesApiSlice';
import { notifications } from '@mantine/notifications';
import { IssueListItem } from './components/IssueListItem';
import { Link } from 'react-router-dom';
import { CreateIssueModal } from './components/CreateIssueModal';

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
      message: 'An error occured while fetching issues',
      color: 'red',
      withCloseButton: true,
    });
  } else if (issues!.length === 0) {
    return (
      <Text size="32px" c="dimmed">
        You don't have any issues yet. <br />
        Start by creating an EPIC
      </Text>
    );
  } else {
    return (
      <Stack w={{ sm: "95%", md: "75%" }} m="auto" mt={16} gap={16}>
        <Group justify='space-between' align='center'>
          <Text size='24px' c="blue">{projectCode}</Text>
          <CreateIssueModal projectCode={projectCode} />
        </Group>
        <Stack gap={0}>
          {issues.map((i) => (
            <Link
              key={i.code}
              to={`/projects/${projectCode}/issues/${i.code}`}
              style={{ textDecoration: 'none', color: 'inherit' }}
            >
              <IssueListItem issue={i} />
            </Link>
          ))}
        </Stack>
      </Stack>
    );
  }
}

import { useParams } from 'react-router-dom';
import ErrorPage from '../ErrorPage/ErrorPage';
import { Loader, Stack } from '@mantine/core';
import { WorklogDetails } from './components/WorklogSection';
import { CommentSection } from './components/CommentSection';
import { AttachmentSection } from './components/AttachmentSection';
import { BasicIssueDetails } from './components/BasicIssueDetails';
import { notifications } from '@mantine/notifications';
import { useGetIssueByCodeQuery } from '../issues/store/issuesApiSlice';

export function IssueDetails() {
    const { projectCode, issueCode } = useParams();
    if (projectCode === undefined || issueCode == undefined) {
        return <ErrorPage />;
    }
    const {
        data: issue,
        error,
        isLoading,
    } = useGetIssueByCodeQuery({
        projectCode,
        issueCode,
    });

    if (isLoading) {
        return <Loader />;
    } else if (error || issue == undefined) {
        notifications.show({
            message: "An error occured while fetching issue",
            color: 'red',
            withCloseButton: true
        })
        return <ErrorPage />;
    }

    return (
        <Stack w='75%' justify='center' align='center' m='auto' mt={24}>
            <BasicIssueDetails issue={issue} />
            <AttachmentSection />
            <WorklogDetails />
            <CommentSection />
        </Stack>
    );
}

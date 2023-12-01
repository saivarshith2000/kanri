import { Link, useNavigate, useParams } from "react-router-dom";
import { Issue, useGetIssueByCodeQuery } from "../../store/issueApiSlice";
import ErrorPage from "@/ErrorPage";
import { Spinner } from "@/components/Spinner";
import { toast } from "react-toastify";
import IssueTypeBadge from "../../components/IssueTypeBadge";
import IssuePriorityBadge from "../../components/IssuePriorityBadge";
import IssueStatusBadge from "../../components/IssueStatusBadge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/shadcnui/ui/tabs";
import CommentSection from "./CommentSection";
import WorklogSection from "./WorklogSection";
import { Button } from "@/shadcnui/ui/button";

export default function IssueDetailsPage() {
  const { projectCode, issueCode } = useParams();
  const navigate = useNavigate();
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
    return <Spinner text="Loading Issues..." />;
  } else if (error || issue == undefined) {
    toast.error("An error occured while fetching issue");
    return <ErrorPage />;
  }
  return (
    <div className="my-2">
      <div className="flex flex-row m-auto w-3/5">
        <Button
          variant="link"
          className="text-blue-600 font-bold"
          onClick={() => navigate(-1)}
        >
          Back to {projectCode}
        </Button>
      </div>
      <div className="m-auto p-4 bg-white w-3/5 rounded-md border-2 border-gray-200">
        <div className="flex flex-col gap-2">
          <div className="flex flex-row gap-2">
            <h2 className="text-blue-600 font-bold">{issue.code}</h2>
            <h2 className="font-bold">{issue.summary}</h2>
          </div>
          <div className="flex flex-col w-1/3 gap-2">
            <div className="flex flex-row justify-between">
              <p>Status</p>
              <IssueStatusBadge status={issue.status} />
            </div>
            <div className="flex flex-row justify-between">
              <p>Type</p>
              <IssueTypeBadge type={issue.type} />
            </div>
            <div className="flex flex-row justify-between">
              <p>Priority</p>
              <IssuePriorityBadge priority={issue.priority} />
            </div>
          </div>
          <h3>{issue.summary}</h3>
        </div>
      </div>
      <div className="m-auto my-4 p-4 bg-white w-3/5 rounded-md border-2 border-gray-200">
        <Tabs defaultValue="comments">
          <TabsList className="w-full justify-start gap-8">
            <TabsTrigger value="comments" className="px-8">
              Comments
            </TabsTrigger>
            <TabsTrigger value="worklogs" className="px-8">
              Work Logs
            </TabsTrigger>
          </TabsList>
          <TabsContent value="comments">
            <CommentSection issueCode={issueCode} projectCode={projectCode} />
          </TabsContent>
          <TabsContent value="worklogs">
            <WorklogSection issueCode={issueCode} projectCode={projectCode} />
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
}

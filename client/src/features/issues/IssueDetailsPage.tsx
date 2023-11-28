import { useParams } from "react-router-dom";
import { Issue, useGetIssueByCodeQuery } from "./store/issueApiSlice";
import ErrorPage from "@/ErrorPage";
import { Spinner } from "@/components/Spinner";
import { toast } from "react-toastify";
import IssueTypeBadge from "./components/IssueTypeBadge";
import IssuePriorityBadge from "./components/IssuePriorityBadge";
import IssueStatusBadge from "./components/IssueStatusBadge";

export default function IssueDetailsPage() {
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
    return <Spinner text="Loading Issues..." />;
  } else if (error || issue == undefined) {
    toast.error("An error occured while fetching issue");
    return <ErrorPage />;
  }
  return (
    <div className="m-auto my-4 p-4 bg-white w-3/5 rounded-md border-2 border-gray-200">
      <div className="flex flex-col gap-2">
        <div className="flex flex-row gap-2">
          <h2 className="text-blue-400 font-bold">{issue.code}</h2>
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
  );
}

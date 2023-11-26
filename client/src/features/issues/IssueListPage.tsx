import { useParams } from "react-router-dom";
import { Issue, useGetIssuesQuery } from "./store/issueApiSlice";
import ErrorPage from "@/ErrorPage";
import { Spinner } from "@/components/Spinner";
import { toast } from "react-toastify";

function IssueList({ issues }: { issues: Issue[] }) {
  if (issues.length === 0) {
    return (
      <div className="text-gray-600 text-2xl text-center w-[400px] m-auto mt-16">
        You don't have any issues yet. Create a new EPIC to get started
      </div>
    );
  }
  return (
    <div className="m-auto p-4 grid grid-cols-3 gap-4">
      {issues.map((i) => (
        <div>{i.description}</div>
      ))}
    </div>
  );
}

export function IssueListPage() {
  const { projectCode } = useParams();
  if (projectCode === undefined) {
    return <ErrorPage />;
  }
  const { data, error, isLoading } = useGetIssuesQuery(projectCode);
  if (isLoading) {
    return <Spinner text="Loading Issues..." />;
  } else if (error) {
    toast.error("An error occured while fetching your projects");
  } else if (data?.length === 0) {
    return <IssueList issues={data} />;
  }

  return <>Viewing issues in project - {projectCode}</>;
}

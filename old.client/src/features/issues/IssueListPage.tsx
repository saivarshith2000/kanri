import { useParams } from "react-router-dom";
import { Issue, useGetIssuesQuery } from "./store/issueApiSlice";
import ErrorPage from "@/ErrorPage";
import { Spinner } from "@/components/Spinner";
import { toast } from "react-toastify";
import { CreateIssueDialog } from "./components/CreateIssueDialog";
import IssueListItem from "./components/IssueListItem";

function IssueList({
  issues,
  projectCode,
}: {
  issues: Issue[];
  projectCode: string;
}) {
  if (issues.length === 0) {
    return (
      <div className="text-gray-600 text-2xl text-center w-[400px] m-auto mt-16">
        You don't have any issues yet. Create a new EPIC to get started
      </div>
    );
  }
  return (
    <div className="mb-1">
      {issues.map((i) => (
        <IssueListItem issue={i} projectCode={projectCode} key={i.code} />
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
    toast.error("An error occured while fetching issues");
  } else {
    return (
      <div className="flex flex-col w-4/5 m-auto my-4">
        <div className="flex flex-row  justify-between items-center">
          <p className="text-3xl text-gray-800">{projectCode}</p>
          <CreateIssueDialog projectCode={projectCode} />
        </div>
        {data?.length === 0 ? (
          <p className="text-4xl text-gray-600 my-4 text-center">
            You don't have any issues yet. <br />
            Start by creating an EPIC
          </p>
        ) : (
          <div className="my-4">
            <IssueList issues={data!} projectCode={projectCode} />
          </div>
        )}
      </div>
    );
  }
}

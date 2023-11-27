import { useParams } from "react-router-dom";
import { Issue, useGetIssuesQuery } from "./store/issueApiSlice";
import ErrorPage from "@/ErrorPage";
import { Spinner } from "@/components/Spinner";
import { toast } from "react-toastify";
import { CreateIssueDialog } from "./components/CreateIssueDialog";

function IssueList({ issues }: { issues: Issue[] }) {
  if (issues.length === 0) {
    return (
      <div className="text-gray-600 text-2xl text-center w-[400px] m-auto mt-16">
        You don't have any issues yet. Create a new EPIC to get started
      </div>
    );
  }
  return (
    <div>
      {issues.map((i) => (
        <div className="m-2 p-2 px-4 shadow-md flex flex-row justify-between bg-white rounded-md">
          <div className="flex flex-row gap-2">
            <p className="text-sm font-bold">{i.type}</p>
            <a
              className="text-sm"
              href="/project/<PROJECT-CODE>/issues/<ISSUE-CODE>"
            >
              {i.code}
            </a>
            <p className="text-sm">{i.summary}</p>
          </div>
          <div className="flex flex-row gap-2">
            <p className="text-sm font-bold">{i.priority}</p>
            <p className="text-sm font-bold">{i.status}</p>
          </div>
        </div>
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
  } else {
    return (
      <div className="flex flex-col w-4/5 m-auto my-4">
        <div className="flex flex-row  justify-between items-center">
          <p className="text-3xl text-gray-800">{projectCode}</p>
          <CreateIssueDialog projectCode={projectCode} />
        </div>
        {data?.length === 0 ? (
          <div>You don't have any issues yet. Start by creating an EPIC</div>
        ) : (
          <IssueList issues={data!} />
        )}
      </div>
    );
  }
}

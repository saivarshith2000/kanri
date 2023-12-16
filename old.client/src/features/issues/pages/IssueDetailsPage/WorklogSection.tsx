import { Spinner } from "@/components/Spinner";
import { Worklog, useGetWorklogsQuery } from "../../store/WorklogApiSlice";
import { format, parseISO } from "date-fns";
import { CreateWorklogDialog } from "./CreateWorklogDialog";

export type worklogSectionParams = {
  issueCode: string;
  projectCode: string;
};

function formatCreatedAt(date: string) {
  try {
    const parsedDate = parseISO(date);
    const timeString = format(parsedDate, "h:mm a");
    const dateString = format(parsedDate, "do/MMM/yyyy");
    return `At ${timeString} On ${dateString}`;
  } catch (error) {
    return "";
  }
}

function WorklogCard({ worklog }: { worklog: Worklog }) {
  return (
    <div className="p-2 mx-1 flex flex-row space-x-2">
      <p className="bg-gray-200 font-bold p-2 self-center rounded-md">
        {worklog.story_points_spent} SP
      </p>
      <div className="flex-1">
        <div className="flex flex-row justify-between items-center">
          <p className="font-bold">{worklog.user_email}</p>
          <p className="text-gray-600 text-sm">
            {formatCreatedAt(worklog.started_at)}
          </p>
        </div>
        <p>{worklog.description}</p>
      </div>
    </div>
  );
}

export default function WorklogSection({
  issueCode,
  projectCode,
}: worklogSectionParams) {
  const {
    data: worklogs,
    error,
    isLoading,
  } = useGetWorklogsQuery({ projectCode, issueCode });
  if (isLoading) {
    return <Spinner text="Loading worklogs..." />;
  } else if (error) {
    return <span>An error occured while loading worklogs</span>;
  }
  return (
    <div className="space-y-2">
      <div className="flex flex-row items-center justify-between mx-2">
        <p className="text-gray-400 text-sm">
          Work logged on this issue appear here
        </p>
        <CreateWorklogDialog issueCode={issueCode} projectCode={projectCode} />
      </div>
      <div className="divide-y-2 divide-gray-100">
        {worklogs?.map((w) => (
          <WorklogCard worklog={w} key={w.started_at} />
        ))}
      </div>
    </div>
  );
}

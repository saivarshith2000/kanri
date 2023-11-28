import { Link } from "react-router-dom";
import { Issue } from "../store/issueApiSlice";
import IssuePriorityBadge from "./IssuePriorityBadge";
import IssueStatusBadge from "./IssueStatusBadge";
import IssueTypeBadge from "./IssueTypeBadge";

export type IssueListItemProps = {
  issue: Issue;
  projectCode: string;
};

export default function IssueListItem({
  issue,
  projectCode,
}: IssueListItemProps) {
  return (
    <div className="p-2 px-4 flex flex-row justify-between bg-white rounded-md border border-gray-200 hover:bg-blue-100">
      <div className="flex flex-row gap-4">
        <IssueTypeBadge type={issue.type} />
        <Link
          className="text-sm w-[75px] justify-start text-blue-700 hover:underline"
          to={`/project/${projectCode}/issues/${issue.code}`}
        >
          {issue.code}
        </Link>
        <p className="text-sm">{issue.summary}</p>
      </div>
      <div className="flex flex-row gap-2">
        <IssuePriorityBadge priority={issue.priority} />
        <IssueStatusBadge status={issue.status} />
        <p className="bg-gray-200 text-center w-[30px] text-sm font-bold text-gray-800 rounded-md">
          {issue.story_points}
        </p>
      </div>
    </div>
  );
}

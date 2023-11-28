export type IssueStatusBadgeProps = {
  status: "OPEN" | "INPROGRESS" | "CLOSED" | "REJECTED";
};

export default function IssueStatusBadge({ status }: IssueStatusBadgeProps) {
  const bgColor = {
    OPEN: "text-blue-600",
    INPROGRESS: "text-yellow-600",
    CLOSED: "text-green-600",
    REJECTED: "text-gray-600",
  };
  return (
    <span
      className={`p-0.5 px-1 bg-gray-200 text-center rounded-md text-xs font-bold ${bgColor[status]} w-[50px]`}
    >
      {status}
    </span>
  );
}

export type IssueTypeBadgeProps = {
  type: "EPIC" | "DEFECT" | "STORY" | "TASK" | "SPIKE";
};

export default function IssueTypeBadge({ type }: IssueTypeBadgeProps) {
  const bgColor = {
    EPIC: "bg-purple-600",
    DEFECT: "bg-red-600",
    STORY: "bg-blue-600",
    TASK: "bg-gray-600",
    SPIKE: "bg-yellow-600",
  };
  return (
    <span
      className={`p-0.5 px-1 text-white text-center rounded-md text-xs font-bold ${bgColor[type]} w-[50px]`}
    >
      {type}
    </span>
  );
}

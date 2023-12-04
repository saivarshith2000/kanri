export type IssuePriorityBadgeProps = {
  priority: "LOW" | "MEDIUM" | "HIGH" | "BLOCKER";
};

export default function IssuePriorityBadge({
  priority,
}: IssuePriorityBadgeProps) {
  const bgColor = {
    LOW: "bg-blue-600",
    MEDIUM: "bg-yellow-600",
    HIGH: "bg-orange-600",
    BLOCKER: "bg-red-600",
  };
  return (
    <span
      className={`p-0.5 px-1 items-center text-white text-center rounded-md text-xs font-bold ${bgColor[priority]} w-[60px]`}
    >
      {priority}
    </span>
  );
}

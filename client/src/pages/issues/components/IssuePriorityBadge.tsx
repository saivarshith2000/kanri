import { Badge } from "@mantine/core";
import { IssuePriority } from "../store/issuesApiSlice";

const colorMap = {
    "LOW": "blue",
    "MEDIUM": "yellow",
    "HIGH": "orange",
    "BLOCKER": "red",
}

export function IssuePriorityBadge({ priority }: { priority: IssuePriority }) {
    return <Badge variant="filled" color={colorMap[priority]} w="80px">{priority}</Badge>
}
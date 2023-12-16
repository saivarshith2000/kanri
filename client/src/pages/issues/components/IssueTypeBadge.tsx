import { Badge } from "@mantine/core";
import { IssueType } from "../store/issuesApiSlice";



const colorMap = {
    "EPIC": "pink",
    "DEFECT": "red",
    "STORY": "blue",
    "TASK": "gray",
    "SPIKE": "yellow"
}

export function IssueTypeBadge({ type }: { type: IssueType }) {
    return <Badge variant="light" color={colorMap[type]} w='80px'>{type}</Badge>
}
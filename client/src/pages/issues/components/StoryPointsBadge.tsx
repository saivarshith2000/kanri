import { Badge } from '@mantine/core';

export function StoryPointsBadge({ story_points }: { story_points: number }) {
  return (
    <Badge variant="outline" w="40px">
      {story_points}
    </Badge>
  );
}

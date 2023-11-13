package com.kanri.api.dto.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class CreateWorkLogRequest {
    private String description;

    @JsonProperty("started_at")
    private Instant startedAt;

    @JsonProperty("story_points_spent")
    private Double storyPointsSpent;
}

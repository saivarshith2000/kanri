package com.kanri.api.dto.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kanri.api.entity.IssueType;
import com.kanri.api.entity.Priority;
import com.kanri.api.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class IssueResponse {
    private String summary;

    private String description;

    private String code;

    @JsonProperty("project_code")
    private String projectCode;

    @JsonProperty("epic_code")
    private String epicCode;

    @JsonProperty("story_points")
    private Double storyPoints;

    private Priority priority;

    private Status status;

    private IssueType type;

    @JsonProperty("reporter_email")
    private String reporterEmail;

    @JsonProperty("assignee_email")
    private String assigneeEmail;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;
}

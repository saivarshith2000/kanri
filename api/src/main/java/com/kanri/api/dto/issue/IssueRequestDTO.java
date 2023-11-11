package com.kanri.api.dto.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kanri.api.entity.IssueType;
import com.kanri.api.entity.Priority;
import com.kanri.api.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssueRequestDTO {
    @NotNull
    @NotBlank
    private String summary;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    private String code;

    @NotNull
    @NotBlank
    @JsonProperty("story_points")
    private Double storyPoints;

    @NotNull
    @NotBlank
    private Priority priority;

    @NotNull
    @NotBlank
    private Status status;

    @NotNull
    @NotBlank
    private IssueType type;

    @NotNull
    @NotBlank
    @JsonProperty("reporter_email")
    private String reporterEmail;

    @JsonProperty("assignee_email")
    private String assigneeEmail;
}

package com.kanri.api.dto.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kanri.api.entity.IssueType;
import com.kanri.api.entity.Priority;
import com.kanri.api.entity.Status;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateIssueRequest {
    @NotNull
    @NotBlank
    private String summary;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @DecimalMin("0.5")
    @JsonProperty("story_points")
    private Double storyPoints;

    @NotNull
    private Priority priority;

    @NotNull
    private Status status;
    @NotNull
    private IssueType type;
}

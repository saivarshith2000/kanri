package com.kanri.api.dto.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kanri.api.entity.IssueType;
import com.kanri.api.entity.Priority;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateIssueRequest {
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

    @JsonProperty("epic_code")
    private String epicCode;

    @NotNull
    private Priority priority;

    @NotNull
    private IssueType type;

    @Email
    @JsonProperty("assignee_email")
    private String assigneeEmail;


    /**
     * This method is a Bean validator, invoked by hibernate validator, and it's name must start with 'is'
     * This method ensures that all types except EPIC has a non-null epiCode property
     * @return Boolean indicating whether the epicCode field is valid
     */
    @AssertTrue(message = "epic_code is required for all issue types except EPIC")
    private boolean isEpicCodeValid() {
        // Only EPIC issue can have epicCode set to null
        if (type != IssueType.EPIC) return epicCode != null;
        return true;
    }
}

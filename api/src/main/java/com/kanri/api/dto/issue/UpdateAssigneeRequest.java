package com.kanri.api.dto.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAssigneeRequest {
    @Email
    @JsonProperty("assignee_email")
    private String assigneeEmail;
}

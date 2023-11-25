package com.kanri.api.dto.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse {
    private String content;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("created_at")
    private String createdAt;
}

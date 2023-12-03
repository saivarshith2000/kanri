package com.kanri.api.dto.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AttachmentResponse {
    private String name;
    private String type;
    private Long size;
    private byte[] content;
    @JsonProperty("created_at")
    private Instant createdAt;
}

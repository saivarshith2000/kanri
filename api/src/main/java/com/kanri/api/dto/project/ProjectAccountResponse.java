package com.kanri.api.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kanri.api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ProjectAccountResponse {
    private String name;

    private String code;

    private String description;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    private Role role;
}
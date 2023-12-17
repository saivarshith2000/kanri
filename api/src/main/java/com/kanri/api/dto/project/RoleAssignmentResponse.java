package com.kanri.api.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kanri.api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleAssignmentResponse {
    private String uid;
    private String email;
    @JsonProperty("display_name")
    private String displayName;
    private String code;
    private Role role;
}

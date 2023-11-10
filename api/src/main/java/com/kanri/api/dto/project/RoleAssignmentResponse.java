package com.kanri.api.dto.project;

import com.kanri.api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleAssignmentResponse {
    private String uid;
    private String email;
    private String code;
    private Role role;
}

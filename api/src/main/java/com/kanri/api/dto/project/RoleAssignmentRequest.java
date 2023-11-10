package com.kanri.api.dto.project;

import com.kanri.api.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleAssignmentRequest {
    @NotBlank
    @NotNull
    private String uid;

    @NotNull
    private Role role;
}

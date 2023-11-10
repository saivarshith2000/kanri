package com.kanri.api.web;

import com.kanri.api.annotation.ProjectExists;
import com.kanri.api.dto.project.RoleAssignmentRequest;
import com.kanri.api.dto.project.RoleAssignmentResponse;
import com.kanri.api.service.RoleManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Validated
@RequiredArgsConstructor
public class RoleManagementController {
    private final RoleManagementService roleManagementService;

    @GetMapping("/project/{projectCode}")
    public List<RoleAssignmentResponse> getUsersInProject(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            String projectCode
    ) {
        return roleManagementService.getUsersInProject(jwt.getSubject(), projectCode);
    }

    @PostMapping("/project/{projectCode}")
    public RoleAssignmentResponse addUserToProject(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            String projectCode,
            @Valid @RequestBody RoleAssignmentRequest dto
            ) {
        return roleManagementService.addUserToProject(jwt.getSubject(), projectCode, dto);
    }

    @PutMapping("/project/{projectCode}")
    public RoleAssignmentResponse modifyUserRoleInProject(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable String projectCode,
            @Valid @RequestBody RoleAssignmentRequest dto
    ) {
        return roleManagementService.modifyUserRoleInProject(jwt.getSubject(), projectCode, dto);
    }

    @DeleteMapping("/project/{projectCode}")
    public ResponseEntity<?> removeUserFromProject(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable String projectCode,
            @Valid @RequestBody RoleAssignmentRequest dto
    ) {
        return ResponseEntity.ok("This endpoint is not yet operational");
    }
}

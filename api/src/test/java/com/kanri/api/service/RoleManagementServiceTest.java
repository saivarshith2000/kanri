package com.kanri.api.service;

import com.kanri.api.dto.project.RoleAssignmentRequest;
import com.kanri.api.dto.project.RoleAssignmentResponse;
import com.kanri.api.entity.Account;
import com.kanri.api.entity.Project;
import com.kanri.api.entity.RoleAssignment;
import com.kanri.api.entity.Role;
import com.kanri.api.exception.BadRequestException;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.mapper.ProjectMapper;
import com.kanri.api.repository.AccountRepository;
import com.kanri.api.repository.RoleAssignmentRepository;
import com.kanri.api.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleManagementServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private RoleAssignmentRepository roleAssignmentRepository;
    @Spy
    private ProjectMapper mapper = Mappers.getMapper(ProjectMapper.class);
    @InjectMocks
    private RoleManagementService roleManagementService;

    // Common across tests
    private final Account initiator = new Account("initiator-uid", "initiator@test.com");
    private final Account user = new Account("user-uid", "user@test.com");
    private final Project project = new Project("TEST", "TEST", "DESC");

    @Test
    @DisplayName("Get users and their permissions in a project when initator isnt't assigned to the project ")
    void testGetRoleAssignmentsInProject() {
        RoleAssignment initiatorRA = new RoleAssignment(initiator, project, Role.ADMIN);

        when(projectRepository.findByCode(project.getCode())).thenReturn(Optional.of(project));
        when(accountRepository.findByUid(initiator.getUid())).thenReturn(Optional.of(user));
        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            roleManagementService.getUsersInProject(initiator.getUid(), project.getCode());
        });
    }

    @Test
    @DisplayName("Assign role to user in a project")
    void testAddUserToProject() {
        RoleAssignment initiatorRA = new RoleAssignment(initiator, project, Role.ADMIN);

        RoleAssignmentRequest request = new RoleAssignmentRequest(user.getUid(), Role.USER);
        RoleAssignment userPA = new RoleAssignment(user, project, request.getRole());

        when(projectRepository.findByCode(project.getCode())).thenReturn(Optional.of(project));
        when(accountRepository.findByUid(user.getUid())).thenReturn(Optional.of(user));
        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.of(initiatorRA));
        when(roleAssignmentRepository.findByUidAndProjectCode(user.getUid(), project.getCode())).thenReturn(Optional.empty());
        when(roleAssignmentRepository.save(any())).thenReturn(userPA);

        RoleAssignmentResponse dto = roleManagementService.addUserToProject(initiator.getUid(), project.getCode(), request);

        assertThat(dto.getRole()).isEqualTo(request.getRole());
        assertThat(dto.getUid()).isEqualTo(request.getUid());
        assertThat(dto.getCode()).isEqualTo(project.getCode());
    }

    @Test
    @DisplayName("Assign role to user in a project when user already has a role in the same project")
    void testAddUserToProjectWhenUserAlreadyHasARole() {
        RoleAssignment initiatorRA = new RoleAssignment(initiator, project, Role.ADMIN);

        RoleAssignmentRequest request = new RoleAssignmentRequest(user.getUid(), Role.USER);
        RoleAssignment existingRA = new RoleAssignment(user, project, request.getRole());

        when(projectRepository.findByCode(project.getCode())).thenReturn(Optional.of(project));
        when(accountRepository.findByUid(user.getUid())).thenReturn(Optional.of(user));
        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.of(initiatorRA));
        when(roleAssignmentRepository.findByUidAndProjectCode(user.getUid(), project.getCode())).thenReturn(Optional.of(existingRA));

        Exception exception = assertThrows(BadRequestException.class, () -> {
            RoleAssignmentResponse dto = roleManagementService.addUserToProject(initiator.getUid(), project.getCode(), request);
        });
    }

    @Test
    @DisplayName("Assign role to user in a project when initiator isn't part of the project")
    void testAddUserToProjectWhenInitiatorIsNotPartOfProject() {
        RoleAssignmentRequest request = new RoleAssignmentRequest(user.getUid(), Role.USER);
        RoleAssignment userPA = new RoleAssignment(user, project, request.getRole());

        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            RoleAssignmentResponse dto = roleManagementService.addUserToProject(initiator.getUid(), project.getCode(), request);
        });
    }


    @Test
    @DisplayName("Assign role to user in a project when initiator has USER role")
    void testAddUserToProjectWhenInitiatorHasUserRole() {
        RoleAssignment initiatorRA = new RoleAssignment(initiator, project, Role.USER);
        RoleAssignmentRequest request = new RoleAssignmentRequest(user.getUid(), Role.USER);

        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.of(initiatorRA));

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            RoleAssignmentResponse dto = roleManagementService.addUserToProject(initiator.getUid(), project.getCode(), request);
        });
    }

    @Test
    @DisplayName("Assign role to user in a project when initiator doesn't have sufficient permissions")
    void testAddUserToProjectWhenInitiatorHasInsufficientPermissions() {
        RoleAssignment initiatorRA = new RoleAssignment(initiator, project, Role.ADMIN);
        RoleAssignmentRequest request = new RoleAssignmentRequest(user.getUid(), Role.OWNER);

        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.of(initiatorRA));

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            RoleAssignmentResponse dto = roleManagementService.addUserToProject(initiator.getUid(), project.getCode(), request);
        });
    }

    @Test
    @DisplayName("Modify a user's role in a project")
    void modifyUserRoleInProject() {
        RoleAssignment initiatorRA = new RoleAssignment(initiator, project, Role.ADMIN);
        RoleAssignmentRequest request = new RoleAssignmentRequest(user.getUid(), Role.ADMIN);
        RoleAssignment userPA = new RoleAssignment(user, project, request.getRole());
        RoleAssignment existingRA = new RoleAssignment(user, project, request.getRole());

        when(projectRepository.findByCode(project.getCode())).thenReturn(Optional.of(project));
        when(accountRepository.findByUid(user.getUid())).thenReturn(Optional.of(user));
        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.of(initiatorRA));
        when(roleAssignmentRepository.findByUidAndProjectCode(user.getUid(), project.getCode())).thenReturn(Optional.of(existingRA));
        when(roleAssignmentRepository.save(any())).thenReturn(userPA);

        RoleAssignmentResponse dto = roleManagementService.modifyUserRoleInProject(initiator.getUid(), project.getCode(), request);

        assertThat(dto.getRole()).isEqualTo(request.getRole());
        assertThat(dto.getUid()).isEqualTo(request.getUid());
        assertThat(dto.getCode()).isEqualTo(project.getCode());
    }

    @Test
    @DisplayName("Modify a user's role in a project when the user is currently not assigned to the project")
    void modifyUserRoleInProjectWhenUserIsNotCurrentlyAssigned() {
        RoleAssignment initiatorRA = new RoleAssignment(initiator, project, Role.ADMIN);
        RoleAssignmentRequest request = new RoleAssignmentRequest(user.getUid(), Role.ADMIN);
        RoleAssignment userPA = new RoleAssignment(user, project, request.getRole());

        when(projectRepository.findByCode(project.getCode())).thenReturn(Optional.of(project));
        when(accountRepository.findByUid(user.getUid())).thenReturn(Optional.of(user));
        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.of(initiatorRA));
        when(roleAssignmentRepository.findByUidAndProjectCode(user.getUid(), project.getCode())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            RoleAssignmentResponse dto = roleManagementService.modifyUserRoleInProject(initiator.getUid(), project.getCode(), request);
        });
    }

    @Test
    @DisplayName("Modify a user's role in a project when initiator isn't part of the project")
    void modifyUserRoleInProjectWhenInitiatorIsNotPartOfProject() {
        RoleAssignmentRequest request = new RoleAssignmentRequest(user.getUid(), Role.ADMIN);
        RoleAssignment userPA = new RoleAssignment(user, project, request.getRole());

        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            RoleAssignmentResponse dto = roleManagementService.modifyUserRoleInProject(initiator.getUid(), project.getCode(), request);
        });
    }

    @Test
    @DisplayName("Modify a user's role in a project when initiator has USER role in the project")
    void modifyUserRoleInProjectWhenInitiatorInitiatorHasUserRole() {
        RoleAssignment initiatorRA = new RoleAssignment(initiator, project, Role.USER);
        RoleAssignmentRequest request = new RoleAssignmentRequest(user.getUid(), Role.ADMIN);
        RoleAssignment userPA = new RoleAssignment(user, project, request.getRole());

        when(roleAssignmentRepository.findByUidAndProjectCode(initiator.getUid(), project.getCode())).thenReturn(Optional.of(initiatorRA));

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            RoleAssignmentResponse dto = roleManagementService.modifyUserRoleInProject(initiator.getUid(), project.getCode(), request);
        });
    }

    @Test
    @DisplayName("Remove a user role from a project")
    void removeUserFromProject() {
    }
}
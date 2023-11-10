package com.kanri.api.service;

import com.kanri.api.dto.project.RoleAssignmentRequest;
import com.kanri.api.dto.project.RoleAssignmentResponse;
import com.kanri.api.entity.Account;
import com.kanri.api.entity.Project;
import com.kanri.api.entity.RoleAssignment;
import com.kanri.api.entity.Role;
import com.kanri.api.exception.BadRequestException;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.repository.AccountRepository;
import com.kanri.api.repository.RoleAssignmentRepository;
import com.kanri.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleManagementService {
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;

    public List<RoleAssignmentResponse> getUsersInProject(String initiatorUid, String projectCode) {
        Project project = getProjectByCode(projectCode);
        Account initiator = getAccountByUid(initiatorUid);
        RoleAssignment mapping = roleAssignmentRepository
                .findByUidAndProjectCode(initiatorUid, projectCode)
                .orElseThrow(() -> new ForbiddenException("Insufficient permissions"));
        return roleAssignmentRepository.findByProjectCode(projectCode);
    }

    @Transactional
    public RoleAssignmentResponse addUserToProject(String initiatorUid, String projectCode, RoleAssignmentRequest dto) {
        RoleAssignment initiatorMapping = roleAssignmentRepository
                .findByUidAndProjectCode(initiatorUid, projectCode)
                .orElseThrow(() -> new ForbiddenException("Insufficient permissions"));

        Role initiatorRole = initiatorMapping.getRole();
        if (initiatorRole == Role.USER || (dto.getRole() == Role.OWNER && initiatorRole != Role.OWNER)) {
            throw new ForbiddenException("Insufficient permissions");
        }

        Account user = getAccountByUid(dto.getUid());
        Project project = getProjectByCode(projectCode);

        Optional<RoleAssignment> mapping = roleAssignmentRepository.findByUidAndProjectCode(dto.getUid(), projectCode);
        if (mapping.isPresent()) {
            String existingRole = mapping.get().getRole().toString();
            throw new BadRequestException("UID " + dto.getUid() + " is already a " + existingRole + " in project " + projectCode);
        }


        RoleAssignment saved = roleAssignmentRepository.save(new RoleAssignment(user, project, dto.getRole()));
        log.info("Added user {} to project {} with role {}", user.getUid(), projectCode, saved.getRole());
        return new RoleAssignmentResponse(user.getUid(), user.getEmail(), projectCode, saved.getRole());
    }

    @Transactional
    public RoleAssignmentResponse modifyUserRoleInProject(String initiatorUid, String projectCode, RoleAssignmentRequest dto) {
        RoleAssignment initiatorMapping = roleAssignmentRepository
                .findByUidAndProjectCode(initiatorUid, projectCode)
                .orElseThrow(() -> new ForbiddenException("Insufficient permissions"));

        Role initiatorRole = initiatorMapping.getRole();
        if (initiatorRole == Role.USER || (dto.getRole() == Role.OWNER && initiatorRole != Role.OWNER)) {
            throw new ForbiddenException("Insufficient permissions");
        }

        Account user = getAccountByUid(dto.getUid());
        Project project = getProjectByCode(projectCode);

        Optional<RoleAssignment> mapping = roleAssignmentRepository.findByUidAndProjectCode(dto.getUid(), projectCode);
        if (mapping.isEmpty()) {
            throw new BadRequestException("UID " + dto.getUid() + " is not assigned to project " + projectCode);
        }
        RoleAssignment assignment = mapping.get();
        Role previousRole = assignment.getRole();
        assignment.setRole(dto.getRole());
        roleAssignmentRepository.save(assignment);
        log.info("Modified user {} role in project {} from {} to {}", user.getUid(), projectCode, previousRole, assignment.getRole());
        return new RoleAssignmentResponse(user.getUid(), user.getEmail(), projectCode, assignment.getRole());
    }

    public void removeUserFromProject(String initiatorUid, String projectCode, String userUid) {
        throw new UnsupportedOperationException("Delete Role Assignment is not implemented yet");
    }

    private Account getAccountByUid(String uid) {
        return accountRepository
                .findByUid(uid)
                .orElseThrow(() -> new NotFoundException("Account with UID " + uid + " not found"));
    }

    private Project getProjectByCode(String code) {
        return projectRepository
                .findByCode(code)
                .orElseThrow(() -> new NotFoundException("Project with code " + code + " not found"));
    }
}

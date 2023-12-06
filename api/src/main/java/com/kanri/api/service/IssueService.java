package com.kanri.api.service;

import com.kanri.api.dto.issue.CreateIssueRequest;
import com.kanri.api.dto.issue.IssueRequestDTO;
import com.kanri.api.dto.issue.IssueResponse;
import com.kanri.api.dto.issue.UpdateIssueRequest;
import com.kanri.api.entity.*;
import com.kanri.api.exception.BadRequestException;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.mapper.IssueMapper;
import com.kanri.api.projection.IssueResponseProjection;
import com.kanri.api.repository.AccountRepository;
import com.kanri.api.repository.IssueRepository;
import com.kanri.api.repository.ProjectRepository;
import com.kanri.api.repository.RoleAssignmentRepository;
import io.grpc.util.RoundRobinLoadBalancer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;
    private final IssueMapper mapper;

    public IssueResponse getIssueByCode(String initiatorUid, String projectCode, String issueCode) {
        throwOnInsufficientUserPermissions(initiatorUid, projectCode);
        IssueResponseProjection projection = issueRepository.findIssueByCode(issueCode).orElseThrow(() -> new NotFoundException("Issue " + issueCode + " not found"));
        return mapper.projectionToIssueResponse(projection);
    }

    public List<IssueResponse> getIssuesInProject(String initiatorUid, String projectCode) {
        throwOnInsufficientUserPermissions(initiatorUid, projectCode);
        return issueRepository.findIssuesByProjectCode(projectCode).stream().map(mapper::projectionToIssueResponse).collect(Collectors.toList());
    }

    public List<IssueResponse> getEpicsInProject(String initiatorUid, String projectCode) {
        throwOnInsufficientUserPermissions(initiatorUid, projectCode);
        return issueRepository.findEpicsByProjectCode(projectCode).stream().map(mapper::projectionToIssueResponse).collect(Collectors.toList());
    }

    @Transactional
    public IssueResponse createIssue(String reporterUid, String projectCode, CreateIssueRequest dto) {
        throwOnInsufficientUserPermissions(reporterUid, projectCode);
        Account reporter = findAccountByUid(reporterUid);
        Project project = findProjectByCode(projectCode);
        Issue issue = new Issue();
        issue.setReporter(reporter);
        issue.setProject(project);
        if (dto.getType() != IssueType.EPIC) {
            throwIfIssueDoesNotBelongToProject(projectCode, dto.getEpicCode());
            issue.setEpic(issueRepository.findByCode(dto.getEpicCode()).orElseThrow(() -> new NotFoundException("EPIC " + dto.getEpicCode() + " not found")));
        }
        if (dto.getAssigneeEmail() != null) {
            Account assignee = findAccountByEmail(dto.getAssigneeEmail());
            checkIfAssigneeInProject(assignee.getUid(), projectCode);
            issue.setAssignee(assignee);
        }
        issue.setSummary(dto.getSummary());
        issue.setDescription(dto.getDescription());
        issue.setStoryPoints(dto.getStoryPoints());
        issue.setType(dto.getType());
        issue.setPriority(dto.getPriority());
        issue.setStatus(Status.OPEN);
        issue.setCode(String.format("%s-%d", projectCode, 1 + issueRepository.countByProject(project)));
        Issue saved = issueRepository.save(issue);
        IssueResponse response = mapper.IssueToIssueResponse(saved);
        response.setReporterEmail(reporter.getEmail());
        response.setAssigneeEmail(dto.getAssigneeEmail());
        response.setProjectCode(projectCode);
        return response;
    }

    public IssueResponse updateIssue(String initiatorUid, String projectCode, String issueCode, UpdateIssueRequest dto) {
        throwOnInsufficientUserPermissions(initiatorUid, projectCode);
        if (dto.getType() == IssueType.EPIC) {
            throw new BadRequestException("Cannot convert an issue to EPIC");
        }
        Issue issue = issueRepository.findByCode(issueCode).orElseThrow(() -> new NotFoundException("Issue " + issueCode + " not found"));
        issue.setSummary(dto.getSummary());
        issue.setDescription(dto.getDescription());
        issue.setStoryPoints(dto.getStoryPoints());
        if (!StatusTransitionService.isValid(issue.getStatus(), dto.getStatus())) {
            throw new BadRequestException("Invalid status transition from " + issue.getStatus() + " to " + dto.getStatus());
        }
        issue.setStatus(dto.getStatus());
        issue.setPriority(dto.getPriority());
        issue.setType(dto.getType());
        Issue saved = issueRepository.save(issue);
        return mapper.IssueToIssueResponse(saved);
    }

    public String updateIssueEpic(String initiatorUid, String projectCode, String issueCode, String epicCode) {
        throwOnInsufficientUserPermissions(initiatorUid, projectCode);
        throwIfIssueDoesNotBelongToProject(projectCode, issueCode);
        throwIfIssueDoesNotBelongToProject(projectCode, epicCode);
        Issue issue = issueRepository.findByCode(issueCode).orElseThrow(() -> new NotFoundException("Issue " + issueCode + " not found"));
        Issue epic = issueRepository.findByCode(epicCode).orElseThrow(() -> new NotFoundException("Epic " + epicCode + " not found"));
        if (epic.getType() != IssueType.EPIC) {
            throw new BadRequestException("Issue " + epicCode + " is not an EPiC");
        }
        issue.setEpic(epic);
        issueRepository.save(issue);
        return epicCode;
    }

    public String updateIssueAssignee(String initiatorUid, String projectCode, String issueCode, String assigneeEmail) {
        throwOnInsufficientUserPermissions(initiatorUid, projectCode);
        throwIfIssueDoesNotBelongToProject(projectCode, issueCode);
        Issue issue = issueRepository.findByCode(issueCode).orElseThrow(() -> new NotFoundException("Issue " + issueCode + " not found"));
        Account assignee = accountRepository.findByEmail(assigneeEmail).orElseThrow(() -> new NotFoundException("Account with email " + assigneeEmail + " not found"));
        checkIfAssigneeInProject(assignee.getUid(), projectCode);
        issue.setAssignee(assignee);
        return assigneeEmail;
    }

    private void throwOnInsufficientUserPermissions(String userUid, String projectCode) {
        roleAssignmentRepository.findByUidAndProjectCode(userUid, projectCode).orElseThrow(() -> new ForbiddenException("Insufficient permissions"));
    }

    private void checkIfAssigneeInProject(String assigneeUid, String projectCode) {
        roleAssignmentRepository
                .findByUidAndProjectCode(assigneeUid, projectCode)
                .orElseThrow(() -> new BadRequestException("Assignee doesn't have a role assigned in the project"));
    }

    private void throwIfIssueDoesNotBelongToProject(String projectCode, String issueCode) {
        Optional<String> extractedProjectCode = Arrays.stream(issueCode.split("-")).findFirst();
        if (extractedProjectCode.isEmpty() || !extractedProjectCode.get().equals(projectCode)) {
            throw new BadRequestException(String.format("Issue %s does not belong to project %s", issueCode, projectCode));
        }
    }

    private Account findAccountByUid(String uid) {
        return accountRepository.findByUid(uid).orElseThrow(() -> new NotFoundException("Account with UID " + uid + "not found"));
    }

    private Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Account with email " + email + "not found"));
    }

    private Project findProjectByCode(String code) {
        return projectRepository.findByCode(code).orElseThrow(() -> new NotFoundException("Project " + code + "not found"));
    }
}
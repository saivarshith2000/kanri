package com.kanri.api.service;

import com.kanri.api.dto.issue.CreateIssueRequest;
import com.kanri.api.dto.issue.IssueRequestDTO;
import com.kanri.api.dto.issue.IssueResponse;
import com.kanri.api.entity.*;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.mapper.IssueMapper;
import com.kanri.api.projection.IssueResponseProjection;
import com.kanri.api.repository.AccountRepository;
import com.kanri.api.repository.IssueRepository;
import com.kanri.api.repository.ProjectRepository;
import com.kanri.api.repository.RoleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public IssueResponse createIssue(String reporterUid, String projectCode, CreateIssueRequest dto) {
        throwOnInsufficientUserPermissions(reporterUid, projectCode);
        Account reporter = findAccountByUid(reporterUid);
        Issue issue = new Issue();
        issue.setReporter(reporter);
        issue.setProject(findProjectByCode(projectCode));
        if (dto.getType() != IssueType.EPIC) {
            issue.setEpic(issueRepository.findByCode(dto.getEpicCode()).orElseThrow(() -> new NotFoundException("EPIC " + dto.getEpicCode() + " not found")));
        }
        if (dto.getAssigneeEmail() != null) {
            Account assignee = findAccountByEmail(dto.getAssigneeEmail());
            issue.setAssignee(assignee);
            throwOnInsufficientUserPermissions(assignee.getUid(), projectCode);
        }
        issue.setSummary(dto.getSummary());
        issue.setDescription(dto.getDescription());
        issue.setStoryPoints(dto.getStoryPoints());
        issue.setType(dto.getType());
        issue.setPriority(dto.getPriority());
        issue.setStatus(Status.OPEN);
        issue.setCode(String.format("%s-%d", projectCode, 1 + issueRepository.count()));
        Issue saved = issueRepository.save(issue);
        IssueResponse response = mapper.IssueToIssueResponse(saved);
        response.setReporterEmail(reporter.getEmail());
        response.setAssigneeEmail(dto.getAssigneeEmail());
        response.setProjectCode(projectCode);
        return response;
    }

    private void throwOnInsufficientUserPermissions(String userUid, String projectCode) {
        roleAssignmentRepository.findByUidAndProjectCode(userUid, projectCode).orElseThrow(() -> new ForbiddenException("Insufficient permissions"));
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
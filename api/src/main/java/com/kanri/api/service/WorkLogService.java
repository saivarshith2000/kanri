package com.kanri.api.service;

import com.kanri.api.dto.issue.CreateWorkLogRequest;
import com.kanri.api.dto.issue.WorkLogResponse;
import com.kanri.api.entity.Account;
import com.kanri.api.entity.Issue;
import com.kanri.api.entity.WorkLog;
import com.kanri.api.exception.BadRequestException;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.mapper.WorkLogMapper;
import com.kanri.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkLogService {
    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;
    private final WorkLogRepository workLogRepository;
    private final WorkLogMapper mapper;

    public List<WorkLogResponse> getWorkLogs(String userUid, String projectCode, String issueCode) {
        throwIfIssueDoesNotBelongToProject(projectCode, issueCode);
        throwOnInsufficientUserPermissions(userUid, projectCode);
        return workLogRepository
                .findByIssueCode(issueCode)
                .stream().map(mapper::projectionToResponse)
                .collect(Collectors.toList());
    }

    public WorkLogResponse addWorkLog(String userUid, String projectCode, String issueCode, CreateWorkLogRequest dto){
        throwIfIssueDoesNotBelongToProject(projectCode, issueCode);
        throwOnInsufficientUserPermissions(userUid, projectCode);
        Account account = findAccountByUid(userUid);
        Issue issue = findIssueByCode(issueCode);
        WorkLog workLog = new WorkLog(
                dto.getDescription(),
                dto.getStartedAt(),
                dto.getStoryPointsSpent(),
                account,
                issue
        );
        WorkLog saved = workLogRepository.save(workLog);
        WorkLogResponse response = mapper.entityToResponse(saved);
        response.setUserEmail(account.getEmail());
        return response;
    }

    public Object editWorkLog() {
        return null;
    }

    public Object removeWorkLog() {
        return null;
    }

    private void throwIfIssueDoesNotBelongToProject(String projectCode, String issueCode) {
        Optional<String> extractedProjectCode = Arrays.stream(issueCode.split("-")).findFirst();
        if (extractedProjectCode.isEmpty() || !extractedProjectCode.get().equals(projectCode)) {
            throw new BadRequestException(String.format("Issue %s does not belong to project %s", issueCode, projectCode));
        }
    }

    private void throwOnInsufficientUserPermissions(String userUid, String projectCode) {
        roleAssignmentRepository.findByUidAndProjectCode(userUid, projectCode).orElseThrow(() -> new ForbiddenException("Insufficient permissions"));
    }

    private Account findAccountByUid(String uid) {
        return accountRepository.findByUid(uid).orElseThrow(() -> new NotFoundException("Account with UID " + uid + "not found"));
    }

    private Issue findIssueByCode(String issueCode) {
        return issueRepository.findByCode(issueCode).orElseThrow(() -> new NotFoundException("Issue " + issueCode + "not found"));
    }

    private Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Account with email " + email + "not found"));
    }
}

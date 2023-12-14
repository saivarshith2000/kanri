package com.kanri.api.service;

import com.kanri.api.dto.issue.CommentResponse;
import com.kanri.api.dto.issue.CreateCommentRequest;
import com.kanri.api.dto.issue.WorkLogResponse;
import com.kanri.api.entity.Account;
import com.kanri.api.entity.Comment;
import com.kanri.api.entity.Issue;
import com.kanri.api.entity.WorkLog;
import com.kanri.api.exception.BadRequestException;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.mapper.CommentMapper;
import com.kanri.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper mapper;

    public List<CommentResponse> getComments(String userUid, String projectCode, String issueCode) {
        throwIfIssueDoesNotBelongToProject(projectCode, issueCode);
        throwOnInsufficientUserPermissions(userUid, projectCode);
        return commentRepository
                .findByIssueCode(issueCode)
                .stream().map(mapper::projectionToResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse addComment(String userUid, String projectCode, String issueCode, CreateCommentRequest dto) {
        throwIfIssueDoesNotBelongToProject(projectCode, issueCode);
        throwOnInsufficientUserPermissions(userUid, projectCode);
        Account account = findAccountByUid(userUid);
        Issue issue = findIssueByCode(issueCode);
        Comment comment = new Comment(
                dto.getContent(),
                issue,
                account
        );
        Comment saved = commentRepository.save(comment);
        CommentResponse response = mapper.entityToResponse(saved);
        response.setUserEmail(account.getEmail());
        return response;
    }

    public Object editComment(String userUid, String projectCode, String issueCode, Long commentId) {
        return null;
    }

    public void deleteComment(String userUid, String projectCode, String issueCode, Long commentId) {
        throwIfIssueDoesNotBelongToProject(projectCode, issueCode);
        throwOnInsufficientUserPermissions(userUid, projectCode);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getUser().getUid().equals(userUid)) {
            throw new ForbiddenException("Cannot delete comments created by another user");
        }
        commentRepository.deleteById(commentId);
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
}

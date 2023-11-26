package com.kanri.api.service;

import com.kanri.api.dto.issue.AttachmentResponse;
import com.kanri.api.entity.Attachment;
import com.kanri.api.entity.Issue;
import com.kanri.api.exception.BadRequestException;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.mapper.AttachmentMapper;
import com.kanri.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final IssueRepository issueRepository;
    private final AttachmentRepository attachmentRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;
    private final AttachmentMapper mapper;

    @Value("${kanri.attachment.max_attachments_per_issue}")
    private Integer maxAttachmentsPerIssue;
    @Value("${kanri.attachment.max_attachment_size_mb}")
    private Integer maxAttachmentSizeMb;
    @Value("${kanri.attachment.allowed_types}")
    private List<String> allowedAttachmentTypes;

    public List<AttachmentResponse> getAttachments(String uid, String projectCode, String issueCode) {
        throwIfIssueDoesNotBelongToProject(projectCode, issueCode);
        throwOnInsufficientUserPermissions(uid, projectCode);
        Issue issue = findIssueByCode(issueCode);
        return attachmentRepository
                .findByIssue(issue)
                .stream().map(mapper::projectToResponse)
                .collect(Collectors.toList());
    }

    public AttachmentResponse uploadAttachment(
            String uid, String projectCode, String issueCode,
            String name, String type, Long size, byte[] content) {
        throwIfIssueDoesNotBelongToProject(projectCode, issueCode);
        throwOnInsufficientUserPermissions(uid, projectCode);
        Issue issue = findIssueByCode(issueCode);
        if (size > maxAttachmentSizeMb * 1000000) {
            throw new ForbiddenException("Attachment size cannot exceed " + maxAttachmentSizeMb + " MB");
        }
        if (!allowedAttachmentTypes.contains(type)) {
            throw new ForbiddenException("Attachment type must be one of txt, pdf, png or jpeg");
        }
        if (attachmentRepository.countByIssue(issue) >= maxAttachmentsPerIssue) {
            throw new ForbiddenException("Only " + maxAttachmentsPerIssue + " attachments are allowed per issue");
        }
        Attachment attachment = new Attachment(name, type, size, content, getChecksum(content), issue);
        try {
            return mapper.entityToResponse(attachmentRepository.save(attachment));
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Attachment already uploaded");
        }
    }

    @SneakyThrows
    private String getChecksum(byte[] file) {
        byte[] hash = MessageDigest.getInstance("MD5").digest(file);
        return new BigInteger(1, hash).toString();
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

    private Issue findIssueByCode(String issueCode) {
        return issueRepository.findByCode(issueCode).orElseThrow(() -> new NotFoundException("Issue " + issueCode + "not found"));
    }
}

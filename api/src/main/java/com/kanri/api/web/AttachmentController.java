package com.kanri.api.web;

import com.kanri.api.annotation.ProjectExists;
import com.kanri.api.dto.issue.CreateWorkLogRequest;
import com.kanri.api.dto.issue.WorkLogResponse;
import com.kanri.api.service.AttachmentService;
import com.kanri.api.service.WorkLogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Validated
public class AttachmentController {
    private final AttachmentService attachmentService;

    @GetMapping("/{projectCode}/issues/{issueCode}/attachments")
    @ProjectExists
    public Object getAttachmentsInIssue(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            @ProjectExists String projectCode,
            @PathVariable String issueCode
    ) {
        System.out.println("here in controller");
        return attachmentService.getAttachments(jwt.getSubject(), projectCode, issueCode);
    }

    @SneakyThrows
    @PostMapping("/{projectCode}/issues/{issueCode}/attachments")
    @ProjectExists
    public Object uploadAttachment(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            @ProjectExists String projectCode,
            @PathVariable String issueCode,
            @RequestParam("attachment") MultipartFile file
            ) {
        return attachmentService.uploadAttachment(
                jwt.getSubject(),
                projectCode,
                issueCode,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getBytes()
        );
    }
}
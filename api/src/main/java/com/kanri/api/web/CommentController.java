package com.kanri.api.web;

import com.kanri.api.annotation.ProjectExists;
import com.kanri.api.dto.issue.CreateCommentRequest;
import com.kanri.api.dto.issue.CommentResponse;
import com.kanri.api.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Validated
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{projectCode}/issues/{issueCode}/comments")
    @ProjectExists
    public List<CommentResponse> getCommentsInIssue(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            @ProjectExists String projectCode,
            @PathVariable String issueCode
    ) {
        return commentService.getComments(jwt.getSubject(), projectCode, issueCode);
    }

    @PostMapping("/{projectCode}/issues/{issueCode}/comments")
    public CommentResponse createComment(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            @ProjectExists String projectCode,
            @PathVariable String issueCode,
            @RequestBody CreateCommentRequest dto
    ) {
        return commentService.addComment(jwt.getSubject(), projectCode, issueCode, dto);
    }

    @PutMapping("/{projectCode}/issues/{issueCode}/comments/{commentId}")
    public String editComment(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            @ProjectExists
            String projectCode,
            @PathVariable String issueCode,
            Long workLogId
    ) {
        return "This endpoint is not operational yet";
    }

    @DeleteMapping("/{projectCode}/issues/{issueCode}/comments/{commentId}")
    public String deleteComment(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            @ProjectExists
            String projectCode,
            @PathVariable String issueCode,
            Long workLogId
    ) {
        return "This endpoint is not operational yet";
    }}

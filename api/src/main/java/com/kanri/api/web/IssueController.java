package com.kanri.api.web;

import com.kanri.api.dto.issue.CreateIssueRequest;
import com.kanri.api.dto.issue.IssueRequestDTO;
import com.kanri.api.dto.issue.IssueResponse;
import com.kanri.api.service.IssueService;
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
public class IssueController {
    private final IssueService issueService;

    @GetMapping("/{projectCode}/issues/{issueCode}")
    public IssueResponse getIssueByCode(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            String projectCode,
            @PathVariable String issueCode
    ) {
        return issueService.getIssueByCode(jwt.getSubject(), projectCode, issueCode);
    }

    @GetMapping("/{projectCode}/issues")
    public List<IssueResponse> getIssuesInProject(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            String projectCode
    ) {
        return issueService.getIssuesInProject(jwt.getSubject(), projectCode);
    }

    @PostMapping("/{projectCode}/issues")
    public IssueResponse createIssue(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            String projectCode,
            @Valid @RequestBody CreateIssueRequest dto
            ) {
        return issueService.createIssue(jwt.getSubject(), projectCode, dto);
    }
}

package com.kanri.api.web;

import com.kanri.api.dto.issue.*;
import com.kanri.api.service.IssueService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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

    @GetMapping("/{projectCode}/epics")
    public List<IssueResponse> getEpicsInProject(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            String projectCode
    ) {
        return issueService.getEpicsInProject(jwt.getSubject(), projectCode);
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
    @PutMapping("/{projectCode}/issues/{issueCode}")
    public IssueResponse update(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            String projectCode,
            @PathVariable
            String issueCode,
            @Valid @RequestBody UpdateIssueRequest dto
            ) {
        return issueService.updateIssue(jwt.getSubject(), projectCode, issueCode, dto);
    }

    @PutMapping("/{projectCode}/issues/{issueCode}/epic")
    public String updateEpic(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            String projectCode,
            @PathVariable
            String issueCode,
            @RequestBody
            UpdateEpicRequest dto
    ) {
        return issueService.updateIssueEpic(jwt.getSubject(), projectCode, issueCode, dto.getEpicCode());
    }

    @PutMapping("/{projectCode}/issues/{issueCode}/assignee")
    public String updateAssignee(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            String projectCode,
            @PathVariable
            String issueCode,
            @Valid
            @RequestBody
            UpdateAssigneeRequest dto
    ) {
        return issueService.updateIssueAssignee(jwt.getSubject(), projectCode, issueCode, dto.getAssigneeEmail());
    }
}

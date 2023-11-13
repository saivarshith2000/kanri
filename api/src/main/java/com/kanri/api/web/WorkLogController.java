package com.kanri.api.web;

import com.kanri.api.annotation.ProjectExists;
import com.kanri.api.dto.issue.CreateWorkLogRequest;
import com.kanri.api.dto.issue.IssueResponse;
import com.kanri.api.dto.issue.WorkLogResponse;
import com.kanri.api.service.WorkLogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Validated
public class WorkLogController {
    private final WorkLogService workLogService;

    @GetMapping("/{projectCode}/issues/{issueCode}/worklogs")
    @ProjectExists
    public List<WorkLogResponse> getWorkLogsInIssue(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            @ProjectExists String projectCode,
            @PathVariable String issueCode
    ) {
        return workLogService.getWorkLogs(jwt.getSubject(), projectCode, issueCode);
    }

    @PostMapping("/{projectCode}/issues/{issueCode}/worklogs")
    public WorkLogResponse createWorkLog(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @Pattern(regexp = "^[A-Z]{3,8}$", message = "Code must be UPPERCASE Alphabets and between 3 to 8 characters")
            @PathVariable
            @ProjectExists String projectCode,
            @PathVariable String issueCode,
            @RequestBody CreateWorkLogRequest dto
            ) {
        return workLogService.addWorkLog(jwt.getSubject(), projectCode, issueCode, dto);
    }

    @PutMapping("/{projectCode}/issues/{issueCode}/worklogs/{worklogId}")
    public String editWorkLog(
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

    @DeleteMapping("/{projectCode}/issues/{issueCode}/worklogs/{worklogId}")
    public String deleteWorkLog(
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
}

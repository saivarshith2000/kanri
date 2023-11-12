package com.kanri.api.service;

import com.kanri.api.dto.issue.CreateIssueRequest;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {
    @Mock
    private IssueRepository issueRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private RoleAssignmentRepository roleAssignmentRepository;
    @Spy
    private IssueMapper mapper = Mappers.getMapper(IssueMapper.class);

    @InjectMocks
    private IssueService issueService;

    private Account user = new Account("test-uid", "test-user@test.com");
    private Account reporter = new Account("reporter", "reporter@testcom");
    private Account assignee = new Account("assignee", "assignee@test.com");
    private Project project = new Project("TESTP", "TESTP", "Test Project");
    private RoleAssignment userRA = new RoleAssignment(user, project, Role.USER);
    private RoleAssignment reporterRA = new RoleAssignment(reporter, project, Role.USER);
    private RoleAssignment assigneeRA = new RoleAssignment(assignee, project, Role.USER);
    private final IssueResponseProjection projection = new MockIssueResponseProjection(
            "test issue",
            "test description",
            "TESTP-2",
            2.0,
            Priority.MEDIUM,
            Status.OPEN,
            IssueType.STORY,
            project.getCode(),
            reporter.getEmail(),
            assignee.getEmail()
    );

    private final Issue testEpic = new Issue(
            "Test EPIC",
            "Test EPIC description",
            "TESTP-1",
            30.0,
            Priority.HIGH,
            Status.INPROGRESS,
            IssueType.EPIC,
            null,
            project,
            reporter,
            assignee, null);
    private final CreateIssueRequest createIssueRequest = new CreateIssueRequest(
            "test issue",
            "test description",
            2.0,
            testEpic.getCode(),
            Priority.MEDIUM,
            IssueType.STORY,
            assignee.getEmail()
    );

    @Test
    @DisplayName("Get an issue by it's code when the user doesn't have sufficient permissions")
    void getIssueByCodeWithoutSufficientPermissions() {

        when(roleAssignmentRepository.findByUidAndProjectCode(user.getUid(), project.getCode())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            issueService.getIssueByCode(user.getUid(), project.getCode(), "TESTA-1");
        });
    }

    @Test
    @DisplayName("Get an issue by it's code")
    void getIssueByCode() {

        when(roleAssignmentRepository.findByUidAndProjectCode(user.getUid(), project.getCode())).thenReturn(Optional.of(userRA));
        when(issueRepository.findIssueByCode(projection.getCode())).thenReturn(Optional.of(projection));

        IssueResponse dto = issueService.getIssueByCode(user.getUid(), project.getCode(), projection.getCode());

        assertThat(dto.getCode()).isEqualTo(projection.getCode());
        assertThat(dto.getProjectCode()).isEqualTo(projection.getProjectCode());
        assertThat(dto.getReporterEmail()).isEqualTo(projection.getReporterEmail());
        assertThat(dto.getAssigneeEmail()).isEqualTo(projection.getAssigneeEmail());
    }

    @Test
    @DisplayName("Get Issues in project")
    void getIssuesInProject() {
    }

    @Test
    @DisplayName("Create a new issue in project when reporter doesn't have sufficient permissions")
    void createIssueWhenReporterDoesNotHaveSufficientPermissions() {
        when(roleAssignmentRepository.findByUidAndProjectCode(reporter.getUid(), project.getCode())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            issueService.createIssue(reporter.getUid(), project.getCode(), createIssueRequest);
        });
    }

    @Test
    @DisplayName("Create a new issue in project when assignee doesn't have sufficient permissions")
    void createIssueWhenAssigneeDoesNotHaveSufficientPermissions() {
        when(roleAssignmentRepository.findByUidAndProjectCode(reporter.getUid(), project.getCode())).thenReturn(Optional.of(reporterRA));
        when(roleAssignmentRepository.findByUidAndProjectCode(assignee.getUid(), project.getCode())).thenReturn(Optional.empty());
        when(issueRepository.findByCode(testEpic.getCode())).thenReturn(Optional.of(testEpic));
        when(accountRepository.findByUid(reporter.getUid())).thenReturn(Optional.of(reporter));
        when(accountRepository.findByEmail(assignee.getEmail())).thenReturn(Optional.of(assignee));
        when(projectRepository.findByCode(project.getCode())).thenReturn(Optional.of(project));

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            issueService.createIssue(reporter.getUid(), project.getCode(), createIssueRequest);
        });
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("Create a new issue in project when EPIC does not exist")
    void createIssueWhenEpicDoesNotExist() {
        when(roleAssignmentRepository.findByUidAndProjectCode(reporter.getUid(), project.getCode())).thenReturn(Optional.of(reporterRA));
        when(accountRepository.findByUid(reporter.getUid())).thenReturn(Optional.of(reporter));
        when(projectRepository.findByCode(project.getCode())).thenReturn(Optional.of(project));

        Exception exception = assertThrows(NotFoundException.class, () -> {
            issueService.createIssue(reporter.getUid(), project.getCode(), createIssueRequest);
        });
        assertThat(exception.getMessage().contains("EPIC")).isTrue();
    }

    @Test
    @DisplayName("Create a new EPIC in project")
    void createNewEpic() {
        when(projectRepository.findByCode(project.getCode())).thenReturn(Optional.of(project));
        when(issueRepository.count()).thenReturn(0L);
        when(issueRepository.save(any())).then(returnsFirstArg());
        when(accountRepository.findByUid(reporter.getUid())).thenReturn(Optional.of(reporter));
        when(accountRepository.findByEmail(assignee.getEmail())).thenReturn(Optional.of(assignee));
        when(roleAssignmentRepository.findByUidAndProjectCode(reporter.getUid(), project.getCode())).thenReturn(Optional.of(reporterRA));
        when(roleAssignmentRepository.findByUidAndProjectCode(assignee.getUid(), project.getCode())).thenReturn(Optional.of(assigneeRA));

        CreateIssueRequest request = new CreateIssueRequest(
                "Test Epic",
                "Test Ppic Description",
                20.0,
                null,
                Priority.MEDIUM,
                IssueType.EPIC,
                assignee.getEmail()
        );

        IssueResponse dto = issueService.createIssue(reporter.getUid(), project.getCode(), request);

        assertThat(dto.getAssigneeEmail()).isEqualTo(assignee.getEmail());
        assertThat(dto.getReporterEmail()).isEqualTo(reporter.getEmail());
        assertThat(dto.getCode()).isEqualTo(String.format("%s-%d", project.getCode(), 1L));
        assertThat(dto.getType()).isEqualTo(IssueType.EPIC);
    }

    @Test
    @DisplayName("Create a new non-EPIC issue in project")
    void createNewIssue() {
        when(projectRepository.findByCode(project.getCode())).thenReturn(Optional.of(project));
        when(issueRepository.count()).thenReturn(0L);
        when(issueRepository.save(any())).then(returnsFirstArg());
        when(issueRepository.findByCode(createIssueRequest.getEpicCode())).thenReturn(Optional.of(testEpic));
        when(accountRepository.findByUid(reporter.getUid())).thenReturn(Optional.of(reporter));
        when(accountRepository.findByEmail(assignee.getEmail())).thenReturn(Optional.of(assignee));
        when(roleAssignmentRepository.findByUidAndProjectCode(reporter.getUid(), project.getCode())).thenReturn(Optional.of(reporterRA));
        when(roleAssignmentRepository.findByUidAndProjectCode(assignee.getUid(), project.getCode())).thenReturn(Optional.of(assigneeRA));

        IssueResponse dto = issueService.createIssue(reporter.getUid(), project.getCode(), createIssueRequest);

        assertThat(dto.getAssigneeEmail()).isEqualTo(assignee.getEmail());
        assertThat(dto.getReporterEmail()).isEqualTo(reporter.getEmail());
        assertThat(dto.getCode()).isEqualTo(String.format("%s-%d", project.getCode(), 1L));
        assertThat(dto.getType()).isEqualTo(createIssueRequest.getType());
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class MockIssueResponseProjection implements IssueResponseProjection {
        private final String summary;
        private final String description;
        private final String code;
        private final Double storyPoints;
        private final Priority priority;
        private final Status status;
        private final IssueType type;
        private final String projectCode;
        private final String reporterEmail;
        private final String assigneeEmail;
        private final Instant createdAt = Instant.now();
        private final Instant updatedAt = Instant.now();
    }
}
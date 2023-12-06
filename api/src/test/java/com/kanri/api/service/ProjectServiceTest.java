package com.kanri.api.service;

import com.kanri.api.dto.project.CreateProjectRequest;
import com.kanri.api.dto.project.MyProjectListItemResponse;
import com.kanri.api.dto.project.ProjectDTO;
import com.kanri.api.entity.Account;
import com.kanri.api.entity.Project;
import com.kanri.api.entity.RoleAssignment;
import com.kanri.api.entity.Role;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.mapper.ProjectMapper;
import com.kanri.api.repository.AccountRepository;
import com.kanri.api.repository.RoleAssignmentRepository;
import com.kanri.api.repository.ProjectRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    private final int MAX_PROJECT_LIMIT = 3;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private RoleAssignmentRepository roleAssignmentRepository;
    @Spy
    private ProjectMapper mapper = Mappers.getMapper(ProjectMapper.class);
    @InjectMocks
    private ProjectService projectService;

    /**
     * Since this is a unit test, we are not initializing spring context.
     * So @Value annotation won't populate the maxProjectLimit field in projectService
     * This method sets the value using a setter on projectService
     */
    @BeforeEach
    public void setMaxProjectLimit() {
        projectService.setMaxProjectLimit(MAX_PROJECT_LIMIT);
    }

    @Test
    @DisplayName("Create a project")
    void createProject() {
        Account account = new Account("test-1", "test1@test.com", "test1");
        CreateProjectRequest request = new CreateProjectRequest("TP-A", "TESTA", "Desc");
        Project project = new Project(request.getName(), request.getCode(), request.getDescription());

        when(roleAssignmentRepository.countProjectsOwnedByUid(any())).thenReturn(0);
        when(accountRepository.findByUid(account.getUid())).thenReturn(Optional.of(account));
        when(projectRepository.save(any())).then(returnsFirstArg());
        when(roleAssignmentRepository.save(any())).thenReturn(new RoleAssignment(account, project, Role.OWNER));

        ProjectDTO dto = projectService.createProject(account.getUid(), request);

        assertThat(dto.getName()).isEqualTo(request.getName());
        assertThat(dto.getCode()).isEqualTo(request.getCode());
        assertThat(dto.getDescription()).isEqualTo(request.getDescription());
    }

    @Test
    @DisplayName("Throw ForbiddenException when Creating a project after limit is reached")
    void createProjectAfterReachingLimit() {
        Account account = new Account("test-1", "test1@test.com", "test1");
        CreateProjectRequest request = new CreateProjectRequest("TP-A", "TESTA", "Desc");

        when(roleAssignmentRepository.countProjectsOwnedByUid(any())).thenReturn(MAX_PROJECT_LIMIT);

        Exception exception = assertThrows(ForbiddenException.class, () -> {
            ProjectDTO dto = projectService.createProject(account.getUid(), request);
        });
    }

    @Test
    @DisplayName("Return a list of projects where a user is a part of")
    void getProjectsByUser() {
        MyProjectListItemResponse[] response = {
                new MyProjectListItemResponse("TP-A", "TPA", "Desc", null, null, Role.OWNER),
                new MyProjectListItemResponse("TP-B", "TPB", "Desc", null, null, Role.USER),
                new MyProjectListItemResponse("TP-C", "TPC", "Desc", null, null, Role.ADMIN)
        };
        Account account = new Account("test-uid-1", "test@testmail.com", "test1");
        when(roleAssignmentRepository
                .getProjectsByUid(account.getUid()))
                .thenReturn(List.of(response));
        List<MyProjectListItemResponse> dtos = projectService.getProjectsByUser(account.getUid());
        assertThat(dtos.size()).isEqualTo(response.length);
    }
}
package com.kanri.api.service;

import com.kanri.api.dto.project.CreateProjectRequest;
import com.kanri.api.dto.project.ProjectAccountResponse;
import com.kanri.api.dto.project.ProjectDTO;
import com.kanri.api.entity.Account;
import com.kanri.api.entity.Project;
import com.kanri.api.entity.ProjectAccount;
import com.kanri.api.entity.Role;
import com.kanri.api.exception.BadRequestException;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.mapper.ProjectMapper;
import com.kanri.api.repository.AccountRepository;
import com.kanri.api.repository.ProjectAccountRepository;
import com.kanri.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final ProjectAccountRepository projectAccountRepository;
    private final ProjectMapper projectMapper;

    @Value("${kanri.project.max_project_limit}")
    private int maxProjectLimit;

    /**
     * @param uid - Firebase UID of the user
     * @return      List of projects (along with the user's role) where user has a role
     */
    public List<ProjectAccountResponse> getProjectsByUser(String uid) {
        return projectAccountRepository.getProjectsByUid(uid);
    }

    /**
     * @param ownerUid      Firebase UID of the project owner
     * @param dto           Details of the project to be created
     * @return projectDTO   Saved project details
     */
    @Transactional
    public ProjectDTO createProject(String ownerUid, CreateProjectRequest dto) {
        throwIfProjectLimitReached(ownerUid);
        Account owner = accountRepository
                .findByUid(ownerUid)
                .orElseThrow(() -> new NotFoundException("Account with UID " + ownerUid + " not found"));
        Project project = new Project();
        project.setCode(dto.getCode());
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        try {
            Project saved = projectRepository.save(project);
            ProjectAccount projectAccount = new ProjectAccount(owner, saved, Role.OWNER);
            projectAccountRepository.save(projectAccount);
            log.info("Created a new project " + dto.getName() + " with code " + dto.getCode() + " owned by " + ownerUid);
            return projectMapper.toProjectDTO(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Code " + dto.getCode() + " is already taken. Please try another code.");
        }
    }

    /**
     * This method throws an exception if the user has reached available project limit
     * This value is currently determined by the kanri.project.max_project_limit property
     * @param uid Firebase UID of the user
     */
    private void throwIfProjectLimitReached(String uid) {
        int projectsOwnedByUser = projectAccountRepository.countProjectsOwnedByUid(uid);
        if (projectsOwnedByUser >= maxProjectLimit) {
            throw new ForbiddenException("You have reached the limit your project limit.");
        }
    }

    public void setMaxProjectLimit(int maxProjectLimit) {
        this.maxProjectLimit = maxProjectLimit;
    }
}

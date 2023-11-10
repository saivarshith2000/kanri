package com.kanri.api.web;

import com.kanri.api.dto.project.CreateProjectRequest;
import com.kanri.api.dto.project.MyProjectListItemResponse;
import com.kanri.api.dto.project.ProjectDTO;
import com.kanri.api.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/")
    public List<MyProjectListItemResponse> getMyProjects(@AuthenticationPrincipal Jwt jwt) {
        String uid = jwt.getSubject();
        return projectService.getProjectsByUser(uid);
    }

    @PostMapping("/")
    public ProjectDTO createProject(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateProjectRequest dto) {
        String ownerUid = jwt.getSubject();
        return projectService.createProject(ownerUid, dto);
    }
}

package com.kanri.api.mapper;

import com.kanri.api.dto.project.ProjectDTO;
import com.kanri.api.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDTO toProjectDTO(Project project);
}

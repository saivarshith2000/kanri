package com.kanri.api.mapper;

import com.kanri.api.dto.issue.WorkLogResponse;
import com.kanri.api.entity.WorkLog;
import com.kanri.api.projection.WorkLogResponseProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkLogMapper {
    WorkLogResponse projectionToResponse(WorkLogResponseProjection projection);
    WorkLogResponse entityToResponse(WorkLog projection);
}

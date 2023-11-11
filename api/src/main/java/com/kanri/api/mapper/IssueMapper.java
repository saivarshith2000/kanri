package com.kanri.api.mapper;

import com.kanri.api.dto.issue.IssueResponse;
import com.kanri.api.entity.Issue;
import com.kanri.api.projection.IssueResponseProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IssueMapper {
//    @Mapping(target = "assigneeEmail", ignore = true)
//    @Mapping(target = "reporterEmail", ignore = true)
    IssueResponse projectionToIssueResponse(IssueResponseProjection projection);
    IssueResponse IssueToIssueResponse(Issue issue);
}

package com.kanri.api.projection;

import com.kanri.api.entity.IssueType;
import com.kanri.api.entity.Priority;
import com.kanri.api.entity.Status;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public interface IssueResponseProjection {
    String getSummary();
    String getDescription();
    String getCode();
    Double getStoryPoints();
    Priority getPriority();
    Status getStatus();
    IssueType getType();
    @Value("#{target.project.code}")
    String getProjectCode();
    @Value("#{target.reporter.email}")
    String getReporterEmail();
    @Value("#{target.assignee.email}")
    String getAssigneeEmail();
    Instant getCreatedAt();
    Instant getUpdatedAt();
}

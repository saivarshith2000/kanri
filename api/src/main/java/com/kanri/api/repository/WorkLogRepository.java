package com.kanri.api.repository;

import com.kanri.api.entity.WorkLog;
import com.kanri.api.projection.WorkLogResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    @Query("SELECT w FROM WorkLog w WHERE w.issue.code = :issueCode ")
    List<WorkLogResponseProjection> findByIssueCode(@Param("issueCode") String issueCode);
}

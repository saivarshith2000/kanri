package com.kanri.api.repository;

import com.kanri.api.entity.Issue;
import com.kanri.api.entity.Project;
import com.kanri.api.projection.IssueResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query("SELECT i FROM Issue i WHERE i.code = :issueCode")
    Optional<IssueResponseProjection> findIssueByCode(@Param("issueCode") String issueCode);

    @Query("SELECT i FROM Issue i WHERE i.project.code = :projectCode")
    List<IssueResponseProjection> findIssuesByProjectCode(@Param("projectCode") String projectCode);

    @Query("SELECT i FROM Issue i WHERE i.type = 'EPIC' AND i.project.code = :projectCode")
    List<IssueResponseProjection> findEpicsByProjectCode(@Param("projectCode") String projectCode);

    Optional<Issue> findByCode(String issueCode);

    Integer countByProject(Project project);
}

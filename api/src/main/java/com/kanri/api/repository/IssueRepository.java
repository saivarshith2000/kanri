package com.kanri.api.repository;

import com.kanri.api.entity.Issue;
import com.kanri.api.projection.IssueResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query("SELECT i FROM Issue i where i.code = :issueCode")
    Optional<IssueResponseProjection> findIssueProjectionByCode(@Param("issueCode") String issueCode);
    Optional<Issue> findByCode(String issueCode);
}

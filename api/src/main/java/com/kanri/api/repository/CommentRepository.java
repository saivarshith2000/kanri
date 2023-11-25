package com.kanri.api.repository;

import com.kanri.api.entity.Comment;
import com.kanri.api.projection.CommentResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.issue.code = :issueCode ")
    List<CommentResponseProjection> findByIssueCode(@Param("issueCode") String issueCode);
}

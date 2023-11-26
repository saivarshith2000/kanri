package com.kanri.api.repository;

import com.kanri.api.entity.Attachment;
import com.kanri.api.entity.Issue;
import com.kanri.api.projection.AttachmentResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<AttachmentResponseProjection> findByIssue(Issue issue);
    Integer countByIssue(Issue issue);
}

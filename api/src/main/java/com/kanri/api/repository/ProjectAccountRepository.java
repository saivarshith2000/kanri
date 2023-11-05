package com.kanri.api.repository;

import com.kanri.api.dto.project.ProjectAccountResponse;
import com.kanri.api.entity.ProjectAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectAccountRepository extends JpaRepository<ProjectAccount, Long> {
    @Query("SELECT COUNT (*) FROM ProjectAccount pa WHERE pa.role = 'OWNER' AND pa.account.uid = :uid")
    Integer countProjectsOwnedByUid(@Param("uid") String uid);

    @Query("SELECT new com.kanri.api.dto.project.ProjectAccountResponse(p.name, p.code, p.description, p.createdAt, p.updatedAt, pa.role) " +
            "FROM ProjectAccount pa JOIN Project p ON pa.project = p " +
            "WHERE pa.account.uid = :uid")
    List<ProjectAccountResponse> getProjectsByUid(@Param("uid") String uid);
}

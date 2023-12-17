package com.kanri.api.repository;

import com.kanri.api.dto.project.MyProjectListItemResponse;
import com.kanri.api.dto.project.RoleAssignmentResponse;
import com.kanri.api.entity.RoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {
    @Query("SELECT COUNT (*) FROM RoleAssignment ra WHERE ra.role = 'OWNER' AND ra.account.uid = :uid")
    Integer countProjectsOwnedByUid(@Param("uid") String uid);

    @Query("SELECT new com.kanri.api.dto.project.MyProjectListItemResponse(p.name, p.code, p.description, p.createdAt, p.updatedAt, ra.role) " +
            "FROM RoleAssignment ra JOIN Project p ON ra.project = p " +
            "WHERE ra.account.uid = :uid")
    List<MyProjectListItemResponse> getProjectsByUid(@Param("uid") String uid);

    @Query("SELECT ra FROM RoleAssignment ra WHERE ra.account.uid = :uid and ra.project.code = :projectCode")
    Optional<RoleAssignment> findByUidAndProjectCode(@Param("uid") String uid, @Param("projectCode") String projectCode);

    @Query("SELECT new com.kanri.api.dto.project.RoleAssignmentResponse(ra.account.uid, ra.account.email, ra.account.displayName, ra.project.code, ra.role)" +
            " FROM RoleAssignment ra WHERE ra.project.code = :code")
    List<RoleAssignmentResponse> findByProjectCode(@Param("code") String code);
}

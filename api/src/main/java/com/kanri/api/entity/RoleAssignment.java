package com.kanri.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role_assignment",
    uniqueConstraints = {
    @UniqueConstraint(columnNames = {"account_id", "project_id", "role"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleAssignment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private Role role;
}

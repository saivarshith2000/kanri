package com.kanri.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Account extends BaseEntity {
    @NonNull
    @NaturalId
    private String uid;

    @NonNull
    @NaturalId
    private String email;

    @NonNull
    private String displayName;

    @OneToMany(mappedBy = "account")
    List<RoleAssignment> roleAssignments = new ArrayList<>();
}

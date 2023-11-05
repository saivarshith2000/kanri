package com.kanri.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
public class Project extends BaseEntity {
    @NonNull
    private String name;

    @NonNull
    @NaturalId
    private String code;

    @NonNull
    private String description;

    @OneToMany(mappedBy = "project")
    List<ProjectAccount> projectAccounts = new ArrayList<>();
}

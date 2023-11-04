package com.kanri.api.entity;

import jakarta.persistence.Entity;
import org.hibernate.annotations.NaturalId;

@Entity
public class Project extends BaseEntity {
    private String name;

    @NaturalId
    private String code;

    private String description;
}

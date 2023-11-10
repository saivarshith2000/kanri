package com.kanri.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "issue",
    uniqueConstraints = {
    @UniqueConstraint(columnNames = {"code"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Issue {
    private String summary;

    private String description;

    private String code;

    @Column(name = "story_points")
    private Double storyPoints;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private IssueType type;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private Account reporter;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Account assignee;
}

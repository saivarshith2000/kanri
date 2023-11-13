package com.kanri.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "work_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkLog extends BaseEntity{
    private String description;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "story_points_spent")
    private Double storyPointsSpent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;
}

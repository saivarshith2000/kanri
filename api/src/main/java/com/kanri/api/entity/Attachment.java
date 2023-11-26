package com.kanri.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachment",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"issue_id", "checksum"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attachment extends BaseEntity{
    private String name;
    private String type;
    private Long size;
    private byte[] content;
    private String checksum;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;
}

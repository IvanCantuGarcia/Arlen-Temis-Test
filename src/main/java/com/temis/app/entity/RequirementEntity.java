package com.temis.app.entity;

import com.temis.app.model.MessageSource;
import com.temis.app.model.RequirementType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

import static jakarta.persistence.EnumType.STRING;

@Data
@Entity
@Table(name = "requirement")
public class RequirementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(STRING)
    RequirementType requirementType;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @JoinColumn(name = "service_id", nullable = false)
    @ManyToOne(optional = false, targetEntity = ServiceEntity.class)
    ServiceEntity serviceEntity;

    @JoinColumn(nullable = true, name = "document_id")
    @OneToOne(optional = true, targetEntity = DocumentEntity.class)
    DocumentEntity documentEntity;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Timestamp lastModifiedDate;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Timestamp creationDate;
}
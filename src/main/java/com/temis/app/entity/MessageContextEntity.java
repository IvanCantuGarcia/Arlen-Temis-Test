package com.temis.app.entity;

import com.temis.app.converter.JsonConverter;
import com.temis.app.model.MessageSource;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import static jakarta.persistence.EnumType.STRING;

@Builder
@Getter
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class MessageContextEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    String phoneNumber;

    @Column(nullable = false)
    String nickName;

    @Enumerated(STRING)
    MessageSource messageSource;

    @Column(nullable = false, columnDefinition = "text")
    @Convert(converter = JsonConverter.class)
    Map<String, String> request;

    @Setter
    @Nullable
    @JoinColumn(nullable = true)
    @ManyToOne(optional = true, targetEntity = UserEntity.class)
    UserEntity userEntity;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;
}

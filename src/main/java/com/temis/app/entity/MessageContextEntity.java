package com.temis.app.entity;

import com.temis.app.model.MessageSource;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;

@Builder
@Getter
@Entity
@Table(name = "message_context")
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    @Enumerated(STRING)
    MessageSource messageSource;

    @OneToMany(mappedBy = "context", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageContextContentEntity> messageContents;

    @Setter
    @Nullable
    @JoinColumn(nullable = true, name = "user_id")
    @ManyToOne(optional = true, targetEntity = UserEntity.class)
    UserEntity userEntity;

    @Setter
    @Nullable
    @JoinColumn(nullable = true, name = "service_id")
    @ManyToOne(optional = true, targetEntity = ServiceEntity.class)
    ServiceEntity serviceEntity;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Timestamp lastModifiedDate;

    @Column(nullable = false)
    boolean isActive = true;

    public List<String> getBodies(){
        return messageContents.stream().map(MessageContextContentEntity::getBody).toList();
    }
    public List<DocumentEntity> getDocumentEntities(){
        return messageContents.stream().map(MessageContextContentEntity::getDocumentEntity).filter(Objects::nonNull).toList();
    }

    public String getContentAsDebugString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (MessageContextContentEntity messageContent : messageContents) {
            stringBuilder.append(messageContent.getBody()).append('\n');

            if(messageContent.getDocumentEntity() != null){
                stringBuilder.append('[').append(messageContent.getDocumentEntity().getFileType());
                if(messageContent.getDocumentEntity().getDocumentType() != null){
                    stringBuilder.append(':').append(messageContent.getDocumentEntity().getDocumentType().getName());
                }
                stringBuilder.append("]\n");;
            }
        }

        return stringBuilder.toString();
    }
}

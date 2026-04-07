package com.br.equaly.messenger.infrastructure.entity;


import com.br.equaly.messenger.domain.enums.AuditActionType;
import com.br.equaly.messenger.domain.enums.AuditType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Document(collection = "audit")
public record AuditEntity(
        @Id
        String id,
        AuditType type,
        Integer companyId,
        Integer subjectId,
        String subjectCode,
        AuditActionType actionType,
        Map<String, Object> additionalInformation,
        LocalDateTime createdAt,
        String createdBy
) {
    public AuditEntity {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    public AuditEntity(AuditType type, Integer companyId, Integer subjectId,
                       String subjectCode, AuditActionType actionType,
                       Map<String, Object> additionalInformation,
                       LocalDateTime createdAt, String createdBy) {
        this(UUID.randomUUID().toString(), type, companyId, subjectId,
                subjectCode, actionType, additionalInformation,
                createdAt, createdBy);
    }
}
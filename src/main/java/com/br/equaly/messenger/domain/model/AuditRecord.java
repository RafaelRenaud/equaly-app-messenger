package com.br.equaly.messenger.domain.model;


import com.br.equaly.messenger.domain.enums.AuditActionType;
import com.br.equaly.messenger.domain.enums.AuditType;

import java.time.LocalDateTime;
import java.util.Map;

public record AuditRecord(
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
}

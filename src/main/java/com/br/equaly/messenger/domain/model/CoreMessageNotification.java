package com.br.equaly.messenger.domain.model;
import com.br.equaly.messenger.domain.enums.CoreMessageType;

import java.time.LocalDateTime;
import java.util.Map;

public record CoreMessageNotification(
        String id,
        CoreMessageType type,
        Integer companyId,
        String companyName,
        Integer userId,
        String destinationEmail,
        String redirectUri,
        Map<String, Object> additionalInformation,
        LocalDateTime createdAt,
        String createdBy
) {
}
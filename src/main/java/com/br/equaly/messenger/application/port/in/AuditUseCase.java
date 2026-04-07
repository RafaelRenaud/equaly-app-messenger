package com.br.equaly.messenger.application.port.in;

import com.br.equaly.messenger.domain.model.AuditRecord;

public interface AuditUseCase {
    void process(AuditRecord auditRecord);
}
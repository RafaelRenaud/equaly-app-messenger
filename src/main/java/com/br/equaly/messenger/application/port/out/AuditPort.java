package com.br.equaly.messenger.application.port.out;

import com.br.equaly.messenger.domain.model.AuditRecord;

public interface AuditPort {
    void process(AuditRecord auditRecord);
}
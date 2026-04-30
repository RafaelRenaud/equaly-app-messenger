package com.br.equaly.messenger.application.service;

import com.br.equaly.messenger.application.port.in.AuditUseCase;
import com.br.equaly.messenger.application.port.out.AuditPort;
import com.br.equaly.messenger.domain.model.AuditRecord;
import org.springframework.stereotype.Service;

@Service
public class AuditService implements AuditUseCase {

    private final AuditPort auditPort;

    public AuditService(AuditPort auditPort) {
        this.auditPort = auditPort;
    }

    @Override
    public void process(AuditRecord auditRecord) {
        auditPort.process(auditRecord);
    }
}

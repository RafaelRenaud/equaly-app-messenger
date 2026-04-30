package com.br.equaly.messenger.infrastructure.adapter.out.impl;

import com.br.equaly.messenger.application.port.out.AuditPort;
import com.br.equaly.messenger.domain.model.AuditRecord;
import com.br.equaly.messenger.infrastructure.mapper.AuditMapper;
import com.br.equaly.messenger.infrastructure.repository.AuditRepository;
import org.springframework.stereotype.Component;

@Component
public class AuditPortImpl implements AuditPort {

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;

    public AuditPortImpl(AuditRepository auditRepository, AuditMapper auditMapper) {
        this.auditRepository = auditRepository;
        this.auditMapper = auditMapper;
    }

    @Override
    public void process(AuditRecord auditRecord) {
        auditRepository.save(auditMapper.toAuditEntity(auditRecord));
    }
}

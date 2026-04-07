package com.br.equaly.messenger.infrastructure.mapper;

import com.br.equaly.messenger.domain.model.AuditRecord;
import com.br.equaly.messenger.infrastructure.entity.AuditEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    AuditEntity toAuditEntity(AuditRecord auditRecord);
    List<AuditEntity> toAuditEntity(List<AuditRecord> auditRecordList);

    AuditRecord toAuditRecord(AuditEntity auditEntity);
    List<AuditRecord> toAuditRecord(List<AuditEntity> auditEntityList);
}

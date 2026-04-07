package com.br.equaly.messenger.infrastructure.repository;

import com.br.equaly.messenger.infrastructure.entity.AuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditRepository extends MongoRepository<AuditEntity, String> {
}

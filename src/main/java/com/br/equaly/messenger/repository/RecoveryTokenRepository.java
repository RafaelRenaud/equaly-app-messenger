package com.br.equaly.messenger.repository;

import com.br.equaly.messenger.model.entity.RecoveryToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryTokenRepository extends CrudRepository<RecoveryToken, String> {
}

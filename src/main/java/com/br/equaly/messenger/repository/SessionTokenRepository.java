package com.br.equaly.messenger.repository;

import com.br.equaly.messenger.model.entity.SessionToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionTokenRepository extends CrudRepository<SessionToken, String> {
}

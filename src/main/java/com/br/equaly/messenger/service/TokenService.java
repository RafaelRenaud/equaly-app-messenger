package com.br.equaly.messenger.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.br.equaly.messenger.model.entity.RecoveryToken;
import com.br.equaly.messenger.model.entity.SessionToken;

import java.util.Map;

public interface TokenService {
    RecoveryToken getRecoveryToken(String tokenId);

    Map<String, Claim> getTokenInformations(String token) throws JWTVerificationException;

    SessionToken getSessionToken(String sessionId);
}

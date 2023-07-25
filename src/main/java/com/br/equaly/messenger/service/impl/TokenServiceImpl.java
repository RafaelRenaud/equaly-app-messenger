package com.br.equaly.messenger.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.br.equaly.messenger.exception.InvalidTokenException;
import com.br.equaly.messenger.exception.TokenException;
import com.br.equaly.messenger.model.entity.RecoveryToken;
import com.br.equaly.messenger.model.entity.SessionToken;
import com.br.equaly.messenger.repository.RecoveryTokenRepository;
import com.br.equaly.messenger.repository.SessionTokenRepository;
import com.br.equaly.messenger.service.TokenService;
import com.br.equaly.messenger.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${server.security.tokenSecret}")
    private String tokenSecret;

    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;

    @Autowired
    private SessionTokenRepository sessionTokenRepository;

    @Override
    public RecoveryToken getRecoveryToken(String tokenId) {
        return Optional.ofNullable(
                recoveryTokenRepository.findById(tokenId).get()
        ).orElseThrow(TokenException::new);
    }

    @Override
    public Map<String, Claim> getTokenInformations(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(this.tokenSecret);
        return JWT.require(algorithm)
                .withIssuer(Constants.EQUALY_ISSUER)
                .build()
                .verify(token)
                .getClaims();
    }

    @Override
    public SessionToken getSessionToken(String sessionId){
        return Optional.ofNullable(
                sessionTokenRepository.findById(sessionId).get()
        ).orElseThrow(InvalidTokenException::new);
    }
}

package com.br.equaly.messenger.model.entity;

import com.br.equaly.messenger.model.enums.EmailTemplate;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "RecoveryToken")
public class RecoveryToken extends EmailToken{

    private String name;

    private String login;

    private String email;

    private String code;

    public RecoveryToken(String id, EmailTemplate templateType, String timestamp) {
        super(id, templateType, timestamp);
    }
}

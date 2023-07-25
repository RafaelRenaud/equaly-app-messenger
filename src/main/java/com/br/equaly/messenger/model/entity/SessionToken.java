package com.br.equaly.messenger.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RedisHash(value = "SessionToken", timeToLive = 7200)
public class SessionToken implements Serializable {

    @Id
    private String id;

    private String login;

    private String email;

    private String userRole;

    private String userSubRole;

    private String departmentId;

    private String departmentName;

    private String corporationId;

    private String appkey;
}

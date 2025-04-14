package com.br.equaly.messenger.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RecoveryToken implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("username")
    private String username;

    @JsonProperty("companyUsername")
    private String companyUsername;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("companyDisplayName")
    private String companyDisplayName;

    @JsonProperty("companyAlias")
    private String companyAlias;

    @JsonProperty("createdAt")
    private String createdAt;

    public RecoveryToken(String id, String code, Long userId, String email, String username, String companyUsername, String companyName, String companyDisplayName, String companyAlias, String createdAt) {
        this.id = id;
        this.code = code;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.companyUsername = companyUsername;
        this.companyName = companyName;
        this.companyDisplayName = companyDisplayName;
        this.companyAlias = companyAlias;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyUsername() {
        return companyUsername;
    }

    public void setCompanyUsername(String companyUsername) {
        this.companyUsername = companyUsername;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDisplayName() {
        return companyDisplayName;
    }

    public void setCompanyDisplayName(String companyDisplayName) {
        this.companyDisplayName = companyDisplayName;
    }

    public String getCompanyAlias() {
        return companyAlias;
    }

    public void setCompanyAlias(String companyAlias) {
        this.companyAlias = companyAlias;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public RecoveryToken() {
    }

    @Override
    public String toString() {
        return "RecoveryToken{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", companyUsername='" + companyUsername + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyDisplayName='" + companyDisplayName + '\'' +
                ", companyAlias='" + companyAlias + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
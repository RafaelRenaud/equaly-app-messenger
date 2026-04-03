package com.br.equaly.messenger.domain.model;

import com.br.equaly.messenger.domain.enums.AdmMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

public record AdmMessageNotification(
        @JsonProperty("id")
        String id,

        @JsonProperty("messageType")
        AdmMessageType admMessageType,

        @JsonProperty("data")
        Map<String, String> data,

        @JsonProperty("companyName")
        String companyName,

        @JsonProperty("companyDisplayName")
        String companyDisplayName,

        @JsonProperty("companyTradingName")
        String companyTradingName,

        @JsonProperty("companyAlias")
        String companyAlias,

        @JsonProperty("companyDocument")
        String companyDocument,
        @JsonProperty("companyContact")
        String companyContact,

        @JsonProperty("actionAt")
        String actionAt,

        @JsonProperty("actionBy")
        String actionBy

) implements Serializable {
}
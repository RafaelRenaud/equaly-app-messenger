package com.br.equaly.messenger.util;

import com.azure.communication.email.EmailClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AzureEmailClient {

    private EmailClient emailClient;
    private String sender;
}

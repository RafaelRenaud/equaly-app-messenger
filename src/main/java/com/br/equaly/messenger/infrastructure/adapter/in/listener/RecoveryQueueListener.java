package com.br.equaly.messenger.infrastructure.adapter.in.listener;

import com.br.equaly.messenger.EqualyAppMessengerApplication;
import com.br.equaly.messenger.application.port.in.RecoveryUseCase;
import com.br.equaly.messenger.domain.model.RecoveryToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueTrigger;

import java.io.IOException;

public class RecoveryQueueListener {

    private final RecoveryUseCase recoveryUseCase;

    public RecoveryQueueListener() {
        this.recoveryUseCase = EqualyAppMessengerApplication.getContext().getBean(RecoveryUseCase.class);
    }

    @FunctionName("listenRecoveryQueue")
    public void sendRecoveryEmail(
            @QueueTrigger(name = "recoveryMessage", queueName = "recovery", connection = "AzureWebJobsStorage") String message,
            ExecutionContext context
    ) {
        try {
            RecoveryToken recoveryToken = new ObjectMapper().readValue(message, RecoveryToken.class);
            context.getLogger().info("******** MESSAGE RECEIVED: " + recoveryToken.toString() + " ********");
            recoveryUseCase.sendRecoveryEmail(recoveryToken);
        } catch (IOException e) {
            context.getLogger().severe("Failed to Process Message: " + e.getMessage());
        }
    }
}
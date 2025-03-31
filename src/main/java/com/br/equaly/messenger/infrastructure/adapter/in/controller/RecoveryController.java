package com.br.equaly.messenger.infrastructure.adapter.in.controller;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.models.QueueMessageItem;
import com.br.equaly.messenger.application.port.in.RecoveryUseCase;
import com.br.equaly.messenger.domain.model.RecoveryToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RecoveryController {

    private final QueueClient recoveryQueueClient;
    private final RecoveryUseCase recoveryUseCase;

    public RecoveryController(QueueClient recoveryQueueClient, RecoveryUseCase recoveryUseCase) {
        this.recoveryQueueClient = recoveryQueueClient;
        this.recoveryUseCase = recoveryUseCase;
    }

    @Scheduled(fixedRate = 10000)
    public void sendRecoveryEmail() {
        PagedIterable<QueueMessageItem> messages = recoveryQueueClient.receiveMessages(10);

        messages.forEach(
                queueMessageItem -> {
                    try {
                        RecoveryToken recoveryToken = new ObjectMapper().readValue(queueMessageItem.getBody().toBytes(), RecoveryToken.class);
                        System.out.println("******** MESSAGE RECEIVED: " + recoveryToken.toString() + " ********");
                        recoveryUseCase.sendRecoveryEmail(recoveryToken);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    recoveryQueueClient.deleteMessage(queueMessageItem.getMessageId(), queueMessageItem.getPopReceipt());
                }
        );
    }
}

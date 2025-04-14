package com.br.equaly.messenger.infrastructure.adapter.in.listener;

import com.azure.core.util.Context;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.models.QueueMessageItem;
import com.br.equaly.messenger.application.port.in.RecoveryUseCase;
import com.br.equaly.messenger.domain.model.RecoveryToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

@Component
public class RecoveryQueueListener {

    private final RecoveryUseCase recoveryUseCase;
    private final QueueClient recoveryQueueClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = Logger.getLogger(RecoveryQueueListener.class.getName());

    public RecoveryQueueListener(RecoveryUseCase recoveryUseCase, QueueClient recoveryQueueClient, ObjectMapper objectMapper) {
        this.recoveryUseCase = recoveryUseCase;
        this.recoveryQueueClient = recoveryQueueClient;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 15000)
    public void sendRecoveryEmail() {
        try {
            List<QueueMessageItem> messages = recoveryQueueClient
                    .receiveMessages(10, Duration.ofSeconds(30), Duration.ofSeconds(5), Context.NONE)
                    .stream().toList();

            if (messages.isEmpty()) {
                return;
            }

            for (QueueMessageItem message : messages) {
                try {
                    RecoveryToken recoveryToken = new ObjectMapper().readValue(new String(
                            Base64.getDecoder().decode(message.getBody().toString()),
                            StandardCharsets.UTF_8
                    ), RecoveryToken.class);
                    LOGGER.info("******** MESSAGE RECEIVED: " + recoveryToken.toString() + " ********");
                    recoveryUseCase.sendRecoveryEmail(recoveryToken);
                    recoveryQueueClient.deleteMessage(message.getMessageId(), message.getPopReceipt());
                } catch (Exception ex) {
                    LOGGER.severe("Erro ao processar mensagem: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            LOGGER.severe("Erro ao ler da fila: " + ex.getMessage());
        }
    }
}
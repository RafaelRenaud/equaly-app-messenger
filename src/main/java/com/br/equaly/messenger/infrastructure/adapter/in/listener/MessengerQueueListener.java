package com.br.equaly.messenger.infrastructure.adapter.in.listener;

import com.azure.core.util.Context;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.models.QueueMessageItem;
import com.br.equaly.messenger.application.port.in.MessengerUseCase;
import com.br.equaly.messenger.domain.model.MessageNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

@Component
public class MessengerQueueListener {

    private final MessengerUseCase messengerUseCase;
    private final QueueClient messengerQueueClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = Logger.getLogger(MessengerQueueListener.class.getName());

    public MessengerQueueListener(MessengerUseCase messengerUseCase, QueueClient messengerQueueClient, ObjectMapper objectMapper) {
        this.messengerQueueClient = messengerQueueClient;
        this.objectMapper = objectMapper;
        this.messengerUseCase = messengerUseCase;
    }

    @Scheduled(fixedDelay = 15000)
    public void sendRecoveryEmail() {
        try {
            List<QueueMessageItem> messages = messengerQueueClient
                    .receiveMessages(10, Duration.ofSeconds(30), Duration.ofSeconds(5), Context.NONE)
                    .stream().toList();

            if (messages.isEmpty()) {
                return;
            }

            for (QueueMessageItem message : messages) {
                try {
                    MessageNotification messageNotification = new ObjectMapper().readValue(new String(
                            Base64.getDecoder().decode(message.getBody().toString()),
                            StandardCharsets.UTF_8
                    ), MessageNotification.class);
                    LOGGER.info("******** NOTIFICATION RECEIVED: " + messageNotification.toString() + " ********");
                    messengerUseCase.sendMessage(messageNotification);
                    messengerQueueClient.deleteMessage(message.getMessageId(), message.getPopReceipt());
                } catch (Exception ex) {
                    LOGGER.severe("Erro ao processar mensagem: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            LOGGER.severe("Erro ao ler da fila: " + ex.getMessage());
        }
    }
}
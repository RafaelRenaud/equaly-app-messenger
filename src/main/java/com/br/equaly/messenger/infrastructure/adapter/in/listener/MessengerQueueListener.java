package com.br.equaly.messenger.infrastructure.adapter.in.listener;

import com.azure.core.util.Context;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.models.QueueMessageItem;
import com.br.equaly.messenger.application.port.in.AdmMessengerUseCase;
import com.br.equaly.messenger.application.port.in.CoreMessengerUseCase;
import com.br.equaly.messenger.domain.model.AdmMessageNotification;
import com.br.equaly.messenger.domain.model.CoreMessageNotification;
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

    private final AdmMessengerUseCase admMessengerUseCase;
    private final CoreMessengerUseCase coreMessengerUseCase;
    private final QueueClient admMessengerQueueClient;
    private final QueueClient coreMessengerQueueClient;
    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = Logger.getLogger(MessengerQueueListener.class.getName());

    public MessengerQueueListener(AdmMessengerUseCase admMessengerUseCase,
                                  CoreMessengerUseCase coreMessengerUseCase,
                                  QueueClient admMessengerQueueClient,
                                  QueueClient coreMessengerQueueClient,
                                  ObjectMapper objectMapper) {
        this.admMessengerUseCase = admMessengerUseCase;
        this.coreMessengerUseCase = coreMessengerUseCase;
        this.admMessengerQueueClient = admMessengerQueueClient;
        this.coreMessengerQueueClient = coreMessengerQueueClient;
        this.objectMapper = objectMapper;
    }

    @Scheduled(cron = "*/15 * * * * *")
    public void sendAdmNotification() {
        processMessages(
                admMessengerQueueClient,
                AdmMessageNotification.class,
                admMessengerUseCase::sendMessage
        );
    }

    @Scheduled(cron = "*/15 * * * * *")
    public void sendCoreNotification() {
        processMessages(
                coreMessengerQueueClient,
                CoreMessageNotification.class,
                coreMessengerUseCase::sendMessage
        );
    }

    private <T> void processMessages(QueueClient queueClient, Class<T> clazz, java.util.function.Consumer<T> consumer) {
        try {
            List<QueueMessageItem> messages = queueClient
                    .receiveMessages(10, Duration.ofSeconds(30), Duration.ofSeconds(5), Context.NONE)
                    .stream()
                    .toList();

            if (messages.isEmpty()) {
                return;
            }

            messages.stream().forEach(message -> {
                try {
                    T payload = deserializeMessage(message, clazz);

                    LOGGER.info("******** NOTIFICATION RECEIVED: " + payload + " ********");

                    consumer.accept(payload);

                    queueClient.deleteMessage(message.getMessageId(), message.getPopReceipt());

                } catch (Exception ex) {
                    LOGGER.severe("Erro ao processar mensagem: " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            LOGGER.severe("Erro ao ler da fila: " + ex.getMessage());
        }
    }

    private <T> T deserializeMessage(QueueMessageItem message, Class<T> clazz) throws Exception {
        String decoded = new String(
                Base64.getDecoder().decode(message.getBody().toString()),
                StandardCharsets.UTF_8
        );
        return objectMapper.readValue(decoded, clazz);
    }
}
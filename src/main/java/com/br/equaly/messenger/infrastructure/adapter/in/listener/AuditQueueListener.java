package com.br.equaly.messenger.infrastructure.adapter.in.listener;

import com.azure.core.util.Context;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.models.QueueMessageItem;
import com.br.equaly.messenger.application.port.in.AuditUseCase;
import com.br.equaly.messenger.domain.model.AuditRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

@Component
public class AuditQueueListener {

    private final AuditUseCase auditUseCase;
    private final QueueClient auditQueueClient;
    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = Logger.getLogger(AuditQueueListener.class.getName());

    public AuditQueueListener(QueueClient auditQueueClient,
                              ObjectMapper objectMapper,
                              AuditUseCase auditUseCase) {
        this.auditQueueClient = auditQueueClient;
        this.objectMapper = objectMapper;
        this.auditUseCase = auditUseCase;
    }

    @Scheduled(cron = "*/15 * * * * *")
    public void getAuditMessages() {
        try {
            List<QueueMessageItem> messages = auditQueueClient
                    .receiveMessages(32, Duration.ofSeconds(30), Duration.ofSeconds(5), Context.NONE)
                    .stream()
                    .toList();

            if (messages.isEmpty()) {
                return;
            }

            LOGGER.info("******** STARTING AUDIT POLLING ********");

            messages.forEach(message -> {
                try {
                    AuditRecord auditRecord = deserializeMessage(message);
                    auditUseCase.process(auditRecord);
                    auditQueueClient.deleteMessage(
                            message.getMessageId(),
                            message.getPopReceipt()
                    );

                } catch (Exception ex) {
                    LOGGER.severe("Erro ao processar mensagem: " + ex.getMessage());
                }
            });

            LOGGER.info("******** STOPPING AUDIT POLLING ********");

        } catch (Exception ex) {
            LOGGER.severe("Erro ao ler da fila: " + ex.getMessage());
        }
    }

    private AuditRecord deserializeMessage(QueueMessageItem message) throws Exception {
        String decoded = new String(
                Base64.getDecoder().decode(message.getBody().toString()),
                StandardCharsets.UTF_8
        );
        return objectMapper.readValue(decoded, AuditRecord.class);
    }
}
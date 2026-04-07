package com.br.equaly.messenger.infrastructure.config;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditQueueConfig {

    @Value("${azure.app.connection-string}")
    private String connectionString;

    @Value("${azure.app.audit.queue}")
    private String auditQueue;

    @Bean
    public QueueClient auditQueueClient() {
        return new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(auditQueue)
                .buildClient();
    }
}

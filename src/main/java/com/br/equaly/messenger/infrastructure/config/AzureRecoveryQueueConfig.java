package com.br.equaly.messenger.infrastructure.config;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureRecoveryQueueConfig {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.queue.recovery}")
    private String recoveryQueue;

    @Bean
    public QueueClient recoveryQueueClient() {
        return new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(recoveryQueue)
                .buildClient();
    }
}

package com.br.equaly.messenger.infrastructure.config;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecoveryQueueConfig {

    @Value("${azure.messenger.connection-string}")
    private String connectionString;

    @Value("${azure.messenger.recovery.queue}")
    private String recoveryQueue;

    @Bean
    public QueueClient recoveryQueueClient() {
        return new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(recoveryQueue)
                .buildClient();
    }
}

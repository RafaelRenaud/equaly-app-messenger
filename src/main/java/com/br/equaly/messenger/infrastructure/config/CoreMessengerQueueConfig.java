package com.br.equaly.messenger.infrastructure.config;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreMessengerQueueConfig {

    @Value("${azure.messenger.connection-string}")
    private String connectionString;

    @Value("${azure.messenger.core-notification.queue}")
    private String coreNotificationQueue;

    @Bean
    public QueueClient coreMessengerQueueClient() {
        return new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(coreNotificationQueue)
                .buildClient();
    }
}

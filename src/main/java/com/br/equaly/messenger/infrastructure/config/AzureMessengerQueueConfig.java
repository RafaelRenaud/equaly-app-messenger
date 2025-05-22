package com.br.equaly.messenger.infrastructure.config;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureMessengerQueueConfig {

    @Value("${azure.messenger.connection-string}")
    private String connectionString;

    @Value("${azure.messenger.notification}")
    private String recoveryQueue;

    @Bean
    public QueueClient messengerQueueClient() {
        return new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(recoveryQueue)
                .buildClient();
    }
}

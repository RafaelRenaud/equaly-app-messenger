package com.br.equaly.messenger.configuration;

import com.azure.communication.email.EmailClientBuilder;
import com.br.equaly.messenger.util.AzureEmailClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureEmailConfiguration {

    @Value("${spring.azure.communication-services.connection}")
    private String connectionString;

    @Value("${spring.azure.communication-services.sender}")
    private String emailSender;

    @Bean
    public AzureEmailClient configureEmailClient(){
        return new AzureEmailClient(
                new EmailClientBuilder().connectionString(this.connectionString).buildClient(),
                emailSender);
    }
}

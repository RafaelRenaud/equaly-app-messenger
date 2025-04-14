package com.br.equaly.messenger.infrastructure.adapter.out.sender;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.br.equaly.messenger.application.port.out.RecoveryEmailSenderPort;
import com.br.equaly.messenger.domain.model.RecoveryToken;
import com.br.equaly.messenger.util.UtilTools;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.logging.Logger;


@Component
public class RecoveryEmailService implements RecoveryEmailSenderPort {

    private final EmailClient emailClient;
    private final TemplateEngine templateEngine;
    private static final Logger LOGGER = Logger.getLogger(RecoveryEmailService.class.getName());

    public RecoveryEmailService(EmailClient emailClient, TemplateEngine templateEngine) {
        this.emailClient = emailClient;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendRecoveryEmail(RecoveryToken recoveryToken) {
        Context context = new Context();
        context.setVariable("username", recoveryToken.getUsername());
        context.setVariable("createdAt", UtilTools.formatTimestamp(recoveryToken.getCreatedAt()));
        context.setVariable("companyUsername", recoveryToken.getCompanyUsername());
        context.setVariable("companyDisplayName", recoveryToken.getCompanyDisplayName());
        context.setVariable("companyName", recoveryToken.getCompanyName());
        context.setVariable("companyAlias", recoveryToken.getCompanyAlias());
        context.setVariable("code", recoveryToken.getCode());

        String emailBody = templateEngine.process("recovery_template", context);

        EmailMessage emailMessage = new EmailMessage()
                .setSubject("Solicitação de Alteração de Senha - eQualy")
                .setBodyHtml(emailBody)
                .setSenderAddress("DoNotReply@7b57c237-e913-481a-8ad1-2157ec7069e6.azurecomm.net")
                .setToRecipients(new EmailAddress(recoveryToken.getEmail()));
        emailClient.beginSend(emailMessage);
    }
}
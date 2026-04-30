package com.br.equaly.messenger.infrastructure.adapter.out.sender;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.br.equaly.messenger.application.port.out.RecoveryEmailSenderPort;
import com.br.equaly.messenger.domain.enums.RecoveryTokenType;
import com.br.equaly.messenger.domain.model.RecoveryToken;
import com.br.equaly.messenger.util.UtilTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;


@Component
public class RecoveryEmailService implements RecoveryEmailSenderPort {

    private final EmailClient emailClient;
    private final TemplateEngine templateEngine;
    private static final Logger LOGGER = Logger.getLogger(RecoveryEmailService.class.getName());

    @Value("${client.equaly.url}")
    private String resetLink;

    @Value("${azure.communication-service.email}")
    private String equalyEmail;

    public RecoveryEmailService(EmailClient emailClient, TemplateEngine templateEngine) {
        this.emailClient = emailClient;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendRecoveryEmail(RecoveryToken recoveryToken) {
        Context context = new Context();
        String emailBody;

        context.setVariable("username", recoveryToken.getUsername());
        context.setVariable("createdAt", UtilTools.formatTimestamp(recoveryToken.getCreatedAt()));
        context.setVariable("companyUsername", recoveryToken.getCompanyUsername());
        context.setVariable("companyTradingName", recoveryToken.getCompanyTradingName());
        context.setVariable("companyName", recoveryToken.getCompanyName());
        context.setVariable("companyAlias", recoveryToken.getCompanyAlias());

        if(recoveryToken.getRecoveryTokenType().equals(RecoveryTokenType.RAC_RECOVERY)){
            context.setVariable("code", recoveryToken.getCode());
            context.setVariable("resetLink",
                    resetLink.concat("/recovery?token=")
                            .concat(recoveryToken.getId())
                            .concat("&email=")
                            .concat(URLEncoder.encode(recoveryToken.getEmail(), StandardCharsets.UTF_8)));
            emailBody = templateEngine.process("recovery/recovery_template", context);
            EmailMessage emailMessage = new EmailMessage()
                    .setSubject("eQualy - Solicitação de Alteração de Senha")
                    .setBodyHtml(emailBody)
                    .setSenderAddress(equalyEmail)
                    .setToRecipients(new EmailAddress(recoveryToken.getEmail()));
            emailClient.beginSend(emailMessage);
        }else if(recoveryToken.getRecoveryTokenType().equals(RecoveryTokenType.ACCOUNT_RECOVERY)){
            emailBody = templateEngine.process("recovery/account_recovery_template", context);
            EmailMessage emailMessage = new EmailMessage()
                    .setSubject("eQualy - Senha Alterada")
                    .setBodyHtml(emailBody)
                    .setSenderAddress(equalyEmail)
                    .setToRecipients(new EmailAddress(recoveryToken.getEmail()));
            emailClient.beginSend(emailMessage);
        }
    }
}
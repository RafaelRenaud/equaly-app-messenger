package com.br.equaly.messenger.infrastructure.adapter.out.email;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailAttachment;
import com.azure.communication.email.models.EmailMessage;
import com.azure.core.util.BinaryData;
import com.br.equaly.messenger.application.port.out.RecoveryEmailSenderPort;
import com.br.equaly.messenger.domain.model.RecoveryToken;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;


@Component
public class RecoveryEmailService implements RecoveryEmailSenderPort {

    private final EmailClient emailClient;
    private final TemplateEngine templateEngine;

    public RecoveryEmailService(EmailClient emailClient, TemplateEngine templateEngine) {
        this.emailClient = emailClient;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendRecoveryEmail(RecoveryToken recoveryToken) {
        Context context = new Context();
        context.setVariable("username", recoveryToken.getUsername());
        context.setVariable("timestamp", recoveryToken.getCreatedAt());
        context.setVariable("rac", recoveryToken.getCode());

        String emailBody = templateEngine.process("recovery_template", context);

        EmailMessage emailMessage = new EmailMessage()
                .setSubject("Solicitação de Alteração de Senha - eQualy")
                .setBodyHtml(emailBody)
                .setSenderAddress("DoNotReply@7b57c237-e913-481a-8ad1-2157ec7069e6.azurecomm.net")
                .setToRecipients(new EmailAddress(recoveryToken.getEmail()));

        try{
            byte[] logoContent = Base64.getEncoder().encodeToString(
                    Files.readAllBytes(new File("src/main/resources/static/logo_white.png").toPath())
            ).getBytes();
            EmailAttachment attachment = new EmailAttachment(
                    "logo_white.png",
                    "image/png",
                    BinaryData.fromBytes(logoContent)
            ).setContentId("logo");
            emailMessage.setAttachments(attachment);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        emailClient.beginSend(emailMessage);
    }
}
package com.br.equaly.messenger.infrastructure.adapter.out.sender;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.br.equaly.messenger.application.port.out.CoreMessengerPort;
import com.br.equaly.messenger.domain.enums.CoreMessageType;
import com.br.equaly.messenger.domain.model.CoreMessageNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.logging.Logger;


@Component
public class CoreMessengerPortImpl implements CoreMessengerPort {

    @Value("${azure.communication-service.email}")
    private String equalyEmail;

    private final EmailClient emailClient;
    private final TemplateEngine templateEngine;
    private static final Logger LOGGER = Logger.getLogger(CoreMessengerPortImpl.class.getName());

    public CoreMessengerPortImpl(EmailClient emailClient, TemplateEngine templateEngine) {
        this.emailClient = emailClient;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMessage(CoreMessageNotification messageNotification) {
        Context context = new Context(Locale.getDefault(), messageNotification.additionalInformation());
        String emailBody = null;
        String emailTitle = null;
        EmailAddress recipient = new EmailAddress(messageNotification.destinationEmail());

        context.setVariable("companyName", messageNotification.companyName());
        context.setVariable("createdAt", messageNotification.createdAt());
        context.setVariable("createdBy", messageNotification.createdBy());
        context.setVariable("redirectUrl", messageNotification.redirectUri() != null ? "https://equaly.app".concat(messageNotification.redirectUri().toString()) : "");

        if(messageNotification.type().equals(CoreMessageType.OCCUR_CREATED)){
            emailBody = templateEngine.process("core/occur/occur_creation_template", context);
            emailTitle = "eQualy - Cadastro de Ocorrência [".concat(messageNotification.additionalInformation().get("occurCode").toString()).concat("]");
        }

        if(messageNotification.type().equals(CoreMessageType.OCCUR_CREATED_EXTERNAL)){
            emailBody = templateEngine.process("core/occur/occur_creation_ext_template", context);
            emailTitle = "Suporte - Cadastro de Reclamação [".concat(messageNotification.additionalInformation().get("occurCode").toString()).concat("]");
        }

        if(messageNotification.type().equals(CoreMessageType.OCCUR_INSPECTION_REQUESTED)){
            emailBody = templateEngine.process("core/occur/occur_inspection_requested_template", context);
            emailTitle = "eQualy - Inspeção de Ocorrência [".concat(messageNotification.additionalInformation().get("occurCode").toString()).concat("]");
        }

        if(messageNotification.type().equals(CoreMessageType.OCCUR_EDIT_REQUESTED)){
            emailBody = templateEngine.process("core/occur/occur_edit_requested_template", context);
            emailTitle = "eQualy - Solicitação de Edição [".concat(messageNotification.additionalInformation().get("editRequestId").toString()).concat("]");
        }

        if(messageNotification.type().equals(CoreMessageType.OCCUR_EDIT_CLOSED)){
            emailBody = templateEngine.process("core/occur/occur_edit_closed_template", context);
            emailTitle = "eQualy - Finalização de Edição [".concat(messageNotification.additionalInformation().get("editRequestId").toString()).concat("]");
        }

        if(messageNotification.type().equals(CoreMessageType.OCCUR_INSPECTED)){
            emailBody = templateEngine.process("core/occur/occur_inspected_template", context);
            emailTitle = "eQualy - Encerramento de Ocorrência Pendente [".concat(messageNotification.additionalInformation().get("occurCode").toString()).concat("]");
        }

        if(messageNotification.type().equals(CoreMessageType.OCCUR_CLOSED)){
            emailBody = templateEngine.process("core/occur/occur_closed_template", context);
            emailTitle = "eQualy - Ocorrência em Encerramento [".concat(messageNotification.additionalInformation().get("occurCode").toString()).concat("]");
        }

        if(messageNotification.type().equals(CoreMessageType.OCCUR_FEEDBACK_REQUESTED)){
            emailBody = templateEngine.process("core/occur/occur_feedback_requested_template", context);
            emailTitle = "Suporte - Avaliação de Reclamação [".concat(messageNotification.additionalInformation().get("occurCode").toString()).concat("]");
        }

        EmailMessage emailMessage = new EmailMessage()
                .setSubject(emailTitle)
                .setBodyHtml(emailBody)
                .setSenderAddress(equalyEmail)
                .setToRecipients(recipient);
        emailClient.beginSend(emailMessage);

    }
}
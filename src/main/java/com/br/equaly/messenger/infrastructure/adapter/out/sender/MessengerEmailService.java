package com.br.equaly.messenger.infrastructure.adapter.out.sender;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.br.equaly.messenger.application.port.out.MessengerEmailSenderPort;
import com.br.equaly.messenger.domain.enums.MessageType;
import com.br.equaly.messenger.domain.model.MessageNotification;
import com.br.equaly.messenger.util.UtilTools;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Component
public class MessengerEmailService implements MessengerEmailSenderPort {

    private final EmailClient emailClient;
    private final TemplateEngine templateEngine;
    private static final Logger LOGGER = Logger.getLogger(MessengerEmailService.class.getName());

    public MessengerEmailService(EmailClient emailClient, TemplateEngine templateEngine) {
        this.emailClient = emailClient;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMessage(MessageNotification messageNotification) {
        Context context = new Context();
        String emailBody = null;
        String emailTitle = null;
        List<EmailAddress> recipients = new ArrayList<>();

        context.setVariable("companyDisplayName", messageNotification.companyDisplayName());
        context.setVariable("companyName", messageNotification.companyName());
        context.setVariable("companyTradingName", messageNotification.companyTradingName());
        context.setVariable("actionAt", UtilTools.formatTimestamp(messageNotification.actionAt()));
        context.setVariable("actionBy", messageNotification.actionBy());

        if(messageNotification.messageType().equals(MessageType.COMPANY_CREATION) || messageNotification.messageType().equals(MessageType.COMPANY_UPDATE)){
            context.setVariable("companyAlias", messageNotification.companyAlias());
            context.setVariable("companyDocument", UtilTools.formatCnpj(messageNotification.companyDocument()));
            context.setVariable("companyContact", messageNotification.companyContact());

            if(messageNotification.messageType().equals(MessageType.COMPANY_CREATION)){
                emailBody = templateEngine.process("company_creation_template", context);
                emailTitle = "eQualy - Cadastro de Empresa Efetuado";
            }else{
                context.setVariable("companyStatus", (messageNotification.data().get("companyStatus").equals("ACTIVE")? "Ativo" : "Inativo"));
                emailBody = templateEngine.process("company_update_template", context);
                emailTitle = "eQualy - Atualização de Empresa Efetuada";
            }

            recipients.addAll(List.of(
                    new EmailAddress(messageNotification.companyContact()),
                    new EmailAddress(messageNotification.data().get("requesterEmail"))
            ));
        }

        if(messageNotification.messageType().equals(MessageType.COMPANY_CREDENTIAL_CREATION) || messageNotification.messageType().equals(MessageType.COMPANY_CREDENTIAL_INACTIVATION)){
            context.setVariable("companyAlias", messageNotification.companyAlias());
            context.setVariable("companyDocument", UtilTools.formatCnpj(messageNotification.companyDocument()));
            context.setVariable("companyContact", messageNotification.companyContact());
            context.setVariable("credentialType", (messageNotification.data().get("credentialType").equals("ADMINISTRATIVE") ? "Administrativa" : "Operacional"));
            context.setVariable("credentialValue", messageNotification.data().get("credentialValue"));

            if(messageNotification.messageType().equals(MessageType.COMPANY_CREDENTIAL_CREATION)){
                emailBody = templateEngine.process("credential_creation_template", context);
                emailTitle = "eQualy - Cadastro de Credencial Efetuada";
            }else{
                emailBody = templateEngine.process("credential_inactivation_template", context);
                emailTitle = "eQualy - Inativação de Credencial Efetuada";
            }

            recipients.addAll(List.of(
                    new EmailAddress(messageNotification.companyContact()),
                    new EmailAddress(messageNotification.data().get("requesterEmail"))
            ));
        }

        if(messageNotification.messageType().equals(MessageType.DEPARTMENT_CREATION) || messageNotification.messageType().equals(MessageType.DEPARTMENT_UPDATE)){
            context.setVariable("departmentName", messageNotification.data().get("departmentName"));
            context.setVariable("departmentDescription", messageNotification.data().get("departmentDescription"));

            if(messageNotification.messageType().equals(MessageType.DEPARTMENT_CREATION)){
                emailBody = templateEngine.process("department_creation_template", context);
                emailTitle = "eQualy - Cadastro de Departamento Efetuado";
            }else{
                context.setVariable("departmentStatus", (messageNotification.data().get("departmentStatus").equals("ACTIVE")? "Ativo" : "Inativo"));
                emailBody = templateEngine.process("department_update_template", context);
                emailTitle = "eQualy - Atualização de Departamento Efetuado";
            }

            if(messageNotification.companyContact().equals(messageNotification.data().get("requesterEmail"))){
                recipients.add(
                        new EmailAddress(messageNotification.companyContact())
                );
            }else{
                recipients.addAll(List.of(
                        new EmailAddress(messageNotification.companyContact()),
                        new EmailAddress(messageNotification.data().get("requesterEmail"))
                ));
            }

        }

        if(messageNotification.messageType().equals(MessageType.USER_CREATION) || messageNotification.messageType().equals(MessageType.USER_UPDATE)){
            context.setVariable("universalUsername", messageNotification.data().get("universalUsername"));
            context.setVariable("departmentName", messageNotification.data().get("departmentName"));
            context.setVariable("username", messageNotification.data().get("username"));
            context.setVariable("nickname", messageNotification.data().get("nickname"));
            context.setVariable("login", messageNotification.data().get("login"));
            context.setVariable("email", messageNotification.data().get("email"));
            context.setVariable("documentNumber", messageNotification.data().get("documentNumber"));

            if(messageNotification.messageType().equals(MessageType.USER_CREATION)){
                emailBody = templateEngine.process("user_creation_template", context);
                emailTitle = "eQualy - Cadastro de Usuário";
            }else{
                context.setVariable("status", (messageNotification.data().get("status").equals("ACTIVE")? "Ativo" : "Inativo"));
                emailBody = templateEngine.process("user_update_template", context);
                emailTitle = "eQualy - Atualização de Usuário";
            }

            recipients.add(
                    new EmailAddress(messageNotification.data().get("email"))
            );
        }

        EmailMessage emailMessage = new EmailMessage()
                .setSubject(emailTitle)
                .setBodyHtml(emailBody)
                .setSenderAddress("DoNotReply@7b57c237-e913-481a-8ad1-2157ec7069e6.azurecomm.net")
                .setToRecipients(recipients);
        emailClient.beginSend(emailMessage);

    }
}
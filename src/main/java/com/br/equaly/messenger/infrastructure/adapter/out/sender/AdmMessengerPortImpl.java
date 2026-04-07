package com.br.equaly.messenger.infrastructure.adapter.out.sender;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.br.equaly.messenger.application.port.out.AdmMessengerPort;
import com.br.equaly.messenger.domain.enums.AdmMessageType;
import com.br.equaly.messenger.domain.model.AdmMessageNotification;
import com.br.equaly.messenger.util.UtilTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Component
public class AdmMessengerPortImpl implements AdmMessengerPort {

    @Value("${azure.communication-service.email}")
    private String equalyEmail;

    private final EmailClient emailClient;
    private final TemplateEngine templateEngine;
    private static final Logger LOGGER = Logger.getLogger(AdmMessengerPortImpl.class.getName());

    public AdmMessengerPortImpl(EmailClient emailClient, TemplateEngine templateEngine) {
        this.emailClient = emailClient;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMessage(AdmMessageNotification messageNotification) {
        Context context = new Context();
        String emailBody = null;
        String emailTitle = null;
        List<EmailAddress> recipients = new ArrayList<>();

        context.setVariable("companyDisplayName", messageNotification.companyDisplayName());
        context.setVariable("companyName", messageNotification.companyName());
        context.setVariable("companyTradingName", messageNotification.companyTradingName());
        context.setVariable("actionAt", UtilTools.formatTimestamp(messageNotification.actionAt()));
        context.setVariable("actionBy", messageNotification.actionBy());

        if(messageNotification.admMessageType().equals(AdmMessageType.COMPANY_CREATION) || messageNotification.admMessageType().equals(AdmMessageType.COMPANY_UPDATE)){
            context.setVariable("companyAlias", messageNotification.companyAlias());
            context.setVariable("companyDocument", UtilTools.formatCnpj(messageNotification.companyDocument()));
            context.setVariable("companyContact", messageNotification.companyContact());

            if(messageNotification.admMessageType().equals(AdmMessageType.COMPANY_CREATION)){
                emailBody = templateEngine.process("adm/company_creation_template", context);
                emailTitle = "eQualy - Cadastro de Empresa Efetuado";
            }else{
                context.setVariable("companyStatus", (messageNotification.data().get("companyStatus").equals("ACTIVE")? "Ativo" : "Inativo"));
                emailBody = templateEngine.process("adm/company_update_template", context);
                emailTitle = "eQualy - Atualização de Empresa Efetuada";
            }

            recipients.addAll(List.of(
                    new EmailAddress(messageNotification.companyContact()),
                    new EmailAddress(messageNotification.data().get("requesterEmail"))
            ));
        }

        if(messageNotification.admMessageType().equals(AdmMessageType.COMPANY_CREDENTIAL_CREATION) || messageNotification.admMessageType().equals(AdmMessageType.COMPANY_CREDENTIAL_INACTIVATION)){
            context.setVariable("companyAlias", messageNotification.companyAlias());
            context.setVariable("companyDocument", UtilTools.formatCnpj(messageNotification.companyDocument()));
            context.setVariable("companyContact", messageNotification.companyContact());
            context.setVariable("credentialType", (messageNotification.data().get("credentialType").equals("ADMINISTRATIVE") ? "Administrativa" : "Operacional"));
            context.setVariable("credentialValue", messageNotification.data().get("credentialValue"));

            if(messageNotification.admMessageType().equals(AdmMessageType.COMPANY_CREDENTIAL_CREATION)){
                emailBody = templateEngine.process("adm/credential_creation_template", context);
                emailTitle = "eQualy - Cadastro de Credencial Efetuada";
            }else{
                emailBody = templateEngine.process("adm/credential_inactivation_template", context);
                emailTitle = "eQualy - Inativação de Credencial Efetuada";
            }

            recipients.addAll(List.of(
                    new EmailAddress(messageNotification.companyContact()),
                    new EmailAddress(messageNotification.data().get("requesterEmail"))
            ));
        }

        if(messageNotification.admMessageType().equals(AdmMessageType.DEPARTMENT_CREATION) || messageNotification.admMessageType().equals(AdmMessageType.DEPARTMENT_UPDATE)){
            context.setVariable("departmentName", messageNotification.data().get("departmentName"));
            context.setVariable("departmentDescription", messageNotification.data().get("departmentDescription"));

            if(messageNotification.admMessageType().equals(AdmMessageType.DEPARTMENT_CREATION)){
                emailBody = templateEngine.process("adm/department_creation_template", context);
                emailTitle = "eQualy - Cadastro de Departamento Efetuado";
            }else{
                context.setVariable("departmentStatus", (messageNotification.data().get("departmentStatus").equals("ACTIVE")? "Ativo" : "Inativo"));
                emailBody = templateEngine.process("adm/department_update_template", context);
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

        if(messageNotification.admMessageType().equals(AdmMessageType.USER_CREATION) || messageNotification.admMessageType().equals(AdmMessageType.USER_UPDATE)){
            context.setVariable("universalUsername", messageNotification.data().get("universalUsername"));
            context.setVariable("departmentName", messageNotification.data().get("departmentName"));
            context.setVariable("username", messageNotification.data().get("username"));
            context.setVariable("login", messageNotification.data().get("login"));
            context.setVariable("email", messageNotification.data().get("email"));
            context.setVariable("documentNumber", messageNotification.data().get("documentNumber"));

            if(messageNotification.admMessageType().equals(AdmMessageType.USER_CREATION)){
                emailBody = templateEngine.process("adm/user_creation_template", context);
                emailTitle = "eQualy - Cadastro de Usuário";
            }else{
                context.setVariable("status", (messageNotification.data().get("status").equals("ACTIVE")? "Ativo" : "Inativo"));
                emailBody = templateEngine.process("adm/user_update_template", context);
                emailTitle = "eQualy - Atualização de Usuário";
            }

            recipients.add(
                    new EmailAddress(messageNotification.data().get("email"))
            );
        }

        EmailMessage emailMessage = new EmailMessage()
                .setSubject(emailTitle)
                .setBodyHtml(emailBody)
                .setSenderAddress(equalyEmail)
                .setToRecipients(recipients);
        emailClient.beginSend(emailMessage);

    }
}
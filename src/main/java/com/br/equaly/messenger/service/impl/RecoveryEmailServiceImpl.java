package com.br.equaly.messenger.service.impl;

import com.azure.communication.email.models.EmailMessage;
import com.br.equaly.messenger.model.entity.RecoveryToken;
import com.br.equaly.messenger.model.enums.EmailTemplate;
import com.br.equaly.messenger.service.EmailService;
import com.br.equaly.messenger.util.AzureEmailClient;
import com.br.equaly.messenger.util.UtilTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class RecoveryEmailServiceImpl implements EmailService {

    @Autowired
    private AzureEmailClient azureEmailClient;

    @Override
    public void sendEmail(RecoveryToken recoveryToken) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("templates/recovery_template.html");
        String fileContent = new String(FileCopyUtils.copyToByteArray(classPathResource.getInputStream()), StandardCharsets.UTF_8);
        fileContent = fileContent.replace("USER_NAME", recoveryToken.getName());
        fileContent = fileContent.replace("RECOVERY_TIMESTAMP", UtilTools.formatTimestamp(recoveryToken.getTimestamp()));
        fileContent = fileContent.replace("RECOVERY_CODE", recoveryToken.getCode());

        EmailMessage message = new EmailMessage()
                .setSenderAddress(azureEmailClient.getSender())
                .setToRecipients(recoveryToken.getEmail())
                .setSubject("Código de Recuperação: " + recoveryToken.getCode())
                .setBodyHtml(fileContent);

        azureEmailClient.getEmailClient().beginSend(message);
    }

    @Override
    public Boolean validateSending(String emailTemplate,String email, RecoveryToken recoveryToken) {
        if(emailTemplate.equals(EmailTemplate.ACCOUNT_RECOVERY.toString())
                && email.equals(recoveryToken.getEmail())){
            return true;
        }

        return false;
    }
}

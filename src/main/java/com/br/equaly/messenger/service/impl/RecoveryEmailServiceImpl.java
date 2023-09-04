package com.br.equaly.messenger.service.impl;

import com.br.equaly.messenger.model.entity.RecoveryToken;
import com.br.equaly.messenger.model.enums.EmailTemplate;
import com.br.equaly.messenger.service.EmailService;
import com.br.equaly.messenger.util.UtilTools;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class RecoveryEmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendEmail(RecoveryToken recoveryToken) throws IOException, MessagingException {

        ClassPathResource classPathResource = new ClassPathResource("templates/recovery_template.html");
        String fileContent = new String(FileCopyUtils.copyToByteArray(classPathResource.getInputStream()), StandardCharsets.UTF_8);
        fileContent = fileContent.replace("USER_NAME", recoveryToken.getName());
        fileContent = fileContent.replace("RECOVERY_TIMESTAMP", UtilTools.formatTimestamp(recoveryToken.getTimestamp()));
        fileContent = fileContent.replace("RECOVERY_CODE", recoveryToken.getCode());

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setFrom("noreply.equaly@gmail.com");
        messageHelper.setTo(recoveryToken.getEmail());
        messageHelper.setSubject("Código de Recuperação: " + recoveryToken.getCode());
        messageHelper.setText(fileContent, true);
        messageHelper.addInline("logo", new ClassPathResource("static/logo_white.png"));

        emailSender.send(message);
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

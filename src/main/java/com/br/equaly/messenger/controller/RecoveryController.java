package com.br.equaly.messenger.controller;

import com.br.equaly.messenger.exception.InvalidTemplateException;
import com.br.equaly.messenger.exception.InvalidTokenException;
import com.br.equaly.messenger.model.dto.recovery.RecoveryRequestDTO;
import com.br.equaly.messenger.model.entity.RecoveryToken;
import com.br.equaly.messenger.model.enums.EmailTemplate;
import com.br.equaly.messenger.service.impl.RecoveryEmailServiceImpl;
import com.br.equaly.messenger.service.impl.TokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class RecoveryController {

    @Autowired
    private RecoveryEmailServiceImpl recoveryEmailService;

    @Autowired
    private TokenServiceImpl tokenService;

    @JmsListener(destination = "${spring.jms.email.queue-name}")
    public void receiveRecoveryEmail(byte[] message) throws IOException, MessagingException {
        System.out.println("******** MESSAGE RECEIVED: "+ new String(message, StandardCharsets.UTF_8) + " ********");

        RecoveryRequestDTO recoveryRequestDTO = new ObjectMapper().readValue(message, RecoveryRequestDTO.class);

        if(recoveryRequestDTO.messageType().equals(EmailTemplate.ACCOUNT_RECOVERY.toString())){
            RecoveryToken recoveryToken = tokenService.getRecoveryToken(new String(Base64.getDecoder().decode(recoveryRequestDTO.token())));
            if(recoveryEmailService.validateSending(recoveryRequestDTO.messageType(), recoveryRequestDTO.email(), recoveryToken)){
                recoveryEmailService.sendEmail(recoveryToken);
            }else{
                throw new InvalidTokenException();
            }
        }else{
            throw new InvalidTemplateException();
        }
    }

}

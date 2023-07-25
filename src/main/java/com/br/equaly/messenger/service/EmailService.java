package com.br.equaly.messenger.service;

import com.br.equaly.messenger.model.entity.RecoveryToken;

import java.io.IOException;

public interface EmailService {
    void sendEmail(RecoveryToken recoveryToken) throws IOException;
    Boolean validateSending(String emailTemplate,String email, RecoveryToken recoveryToken);
}

package com.br.equaly.messenger.application.service;

import com.br.equaly.messenger.application.port.in.RecoveryUseCase;
import com.br.equaly.messenger.application.port.out.RecoveryEmailSenderPort;
import com.br.equaly.messenger.domain.model.RecoveryToken;
import org.springframework.stereotype.Service;

@Service
public class RecoveryService implements RecoveryUseCase {
    private final RecoveryEmailSenderPort recoveryEmailSenderPort;

    public RecoveryService(RecoveryEmailSenderPort recoveryEmailSenderPort) {
        this.recoveryEmailSenderPort = recoveryEmailSenderPort;
    }

    @Override
    public void sendRecoveryEmail(RecoveryToken recoveryToken) {
        recoveryEmailSenderPort.sendRecoveryEmail(recoveryToken);
    }
}

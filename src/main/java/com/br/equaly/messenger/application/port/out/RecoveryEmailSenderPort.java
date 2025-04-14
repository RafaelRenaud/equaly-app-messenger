package com.br.equaly.messenger.application.port.out;

import com.br.equaly.messenger.domain.model.RecoveryToken;

public interface RecoveryEmailSenderPort {
    void sendRecoveryEmail(RecoveryToken recoveryToken);
}

package com.br.equaly.messenger.application.port.in;

import com.br.equaly.messenger.domain.model.RecoveryToken;

public interface RecoveryUseCase {
    void sendRecoveryEmail(RecoveryToken recoveryToken);
}
package com.br.equaly.messenger.application.port.in;

import com.br.equaly.messenger.domain.model.AdmMessageNotification;

public interface AdmMessengerUseCase {
    void sendMessage(AdmMessageNotification messageNotification);
}
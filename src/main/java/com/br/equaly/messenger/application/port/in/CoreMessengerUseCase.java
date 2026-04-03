package com.br.equaly.messenger.application.port.in;

import com.br.equaly.messenger.domain.model.CoreMessageNotification;

public interface CoreMessengerUseCase {
    void sendMessage(CoreMessageNotification messageNotification);
}
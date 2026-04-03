package com.br.equaly.messenger.application.port.out;

import com.br.equaly.messenger.domain.model.CoreMessageNotification;

public interface CoreMessengerPort {
    void sendMessage(CoreMessageNotification messageNotification);
}

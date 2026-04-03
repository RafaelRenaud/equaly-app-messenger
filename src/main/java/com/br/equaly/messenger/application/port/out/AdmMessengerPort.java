package com.br.equaly.messenger.application.port.out;

import com.br.equaly.messenger.domain.model.AdmMessageNotification;

public interface AdmMessengerPort {
    void sendMessage(AdmMessageNotification messageNotification);
}

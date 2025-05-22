package com.br.equaly.messenger.application.port.out;

import com.br.equaly.messenger.domain.model.MessageNotification;

public interface MessengerEmailSenderPort {
    void sendMessage(MessageNotification messageNotification);
}

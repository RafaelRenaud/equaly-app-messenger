package com.br.equaly.messenger.application.port.in;

import com.br.equaly.messenger.domain.model.MessageNotification;

public interface MessengerUseCase {
    void sendMessage(MessageNotification messageNotification);
}
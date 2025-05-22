package com.br.equaly.messenger.application.service;

import com.br.equaly.messenger.application.port.in.MessengerUseCase;
import com.br.equaly.messenger.application.port.out.MessengerEmailSenderPort;
import com.br.equaly.messenger.domain.model.MessageNotification;
import org.springframework.stereotype.Service;

@Service
public class MessengerService implements MessengerUseCase {
    private final MessengerEmailSenderPort messengerEmailSenderPort;

    public MessengerService(MessengerEmailSenderPort messengerEmailSenderPort) {
        this.messengerEmailSenderPort = messengerEmailSenderPort;
    }

    @Override
    public void sendMessage(MessageNotification messageNotification) {
        messengerEmailSenderPort.sendMessage(messageNotification);
    }
}

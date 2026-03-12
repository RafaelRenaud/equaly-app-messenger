package com.br.equaly.messenger.application.service;

import com.br.equaly.messenger.application.port.in.AdmMessengerUseCase;
import com.br.equaly.messenger.application.port.out.AdmMessengerPort;
import com.br.equaly.messenger.domain.model.MessageNotification;
import org.springframework.stereotype.Service;

@Service
public class AdmMessengerService implements AdmMessengerUseCase {
    private final AdmMessengerPort admMessengerPort;

    public AdmMessengerService(AdmMessengerPort admMessengerPort) {
        this.admMessengerPort = admMessengerPort;
    }

    @Override
    public void sendMessage(MessageNotification messageNotification) {
        admMessengerPort.sendMessage(messageNotification);
    }
}

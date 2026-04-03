package com.br.equaly.messenger.application.service;

import com.br.equaly.messenger.application.port.in.CoreMessengerUseCase;
import com.br.equaly.messenger.application.port.out.CoreMessengerPort;
import com.br.equaly.messenger.domain.model.CoreMessageNotification;
import org.springframework.stereotype.Service;

@Service
public class CoreMessengerService implements CoreMessengerUseCase {
    private final CoreMessengerPort coreMessengerPort;

    public CoreMessengerService(CoreMessengerPort coreMessengerPort) {
        this.coreMessengerPort = coreMessengerPort;
    }

    @Override
    public void sendMessage(CoreMessageNotification messageNotification) {
        coreMessengerPort.sendMessage(messageNotification);
    }
}

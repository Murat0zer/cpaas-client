package com.netas.cpaas.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

@Slf4j
public class NotificationStompSessionHandler implements StompSessionHandler {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
      log.info("Web socket created " + session.getSessionId());
      log.info("Headers...\n" + connectedHeaders.toSingleValueMap().toString() );
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Error");
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Error");
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return headers.getContentType().getClass();
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {

    }
}

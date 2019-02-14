package com.netas.cpaas.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.lang.reflect.Type;

@Component
@Slf4j
public class NotificationStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
      log.info("Connected... " + session.getSessionId());
      log.info("Headers...\n" + connectedHeaders.toSingleValueMap().toString() );
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Error");
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

        log.error(exception.getMessage(), exception.getCause());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {

        return headers.getContentType().getClass();
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("hi");
    }

    @EventListener(SessionConnectedEvent.class)
    public void handleWebsocketConnectListner(SessionConnectedEvent event) {
        log.info("Received a new web socket connection...");
        log.info(event.getSource().toString());
    }


}

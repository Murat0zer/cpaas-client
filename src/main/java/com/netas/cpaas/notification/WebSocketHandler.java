package com.netas.cpaas.notification;

import com.netas.cpaas.SpringContext;
import com.netas.cpaas.chat.ChatService;
import com.netas.cpaas.chat.model.message.ChatMessage;
import com.netas.cpaas.chat.model.message.ChatMessageJson;
import com.netas.cpaas.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.socket.*;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
@Slf4j
public class WebSocketHandler extends AbstractWebSocketHandler {

    private final ChatService chatService = SpringContext.getBean(ChatService.class);

    private final SimpMessagingTemplate simpMessagingTemplate = SpringContext.getBean(SimpMessagingTemplate.class);
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info(message.getPayload());
        StandardWebSocketSession nativeWebSocketSession = (StandardWebSocketSession) session;
        Object authObject = nativeWebSocketSession.getNativeSession().getUserProperties().get("auth");
        Authentication authentication = (Authentication) authObject;
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        ChatMessage chatMessage = ChatMessage.builder().text(message.getPayload()).build();

        ChatMessageJson chatMessageJson = ChatMessageJson.builder().chatMessage(chatMessage).build();
        simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/notifications/{username}", chatMessageJson);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        session.sendMessage(new PongMessage());
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        log.error(exception.getMessage(), exception.getCause());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

    }
}

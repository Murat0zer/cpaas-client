package com.netas.cpaas.notification;

import com.netas.cpaas.chat.ChatService;
import com.netas.cpaas.chat.model.ChatMessage;
import com.netas.cpaas.chat.model.notification.ChatMessageJson;
import com.netas.cpaas.user.model.User;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.socket.*;
import org.springframework.web.socket.adapter.NativeWebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
@Slf4j
public class WebSocketHandler extends AbstractWebSocketHandler {

    private final ChatService chatService = SpringContext.getBean(ChatService.class);

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
        Authentication authentication = (Authentication) nativeWebSocketSession.getNativeSession().getUserProperties().get("auth");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        ChatMessage chatMessage = ChatMessage.builder().textMessage("Mesaj alindi :)").build();
        ChatMessageJson chatMessageJson = ChatMessageJson.builder().chatMessage(chatMessage).build();
        try {
            chatService.sendMessage(user.getNvsUser().getPreferredUsername(), "user2@nts.ipa4.att.com", chatMessageJson);
        } catch (HttpClientErrorException e) {
            log.error(e.getResponseBodyAsString(), e.getStatusText());
        }
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

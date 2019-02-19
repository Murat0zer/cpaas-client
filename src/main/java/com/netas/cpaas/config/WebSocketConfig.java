package com.netas.cpaas.config;

import com.netas.cpaas.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.Objects;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/notifications");

        config.setApplicationDestinationPrefixes("/user");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/websocket").setAllowedOrigins("*");


    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                log.info("in override " + accessor.getCommand());

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

//                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//                    String name = auth.getName(); //get logged in username
//                    System.out.println("Authenticated User : " + name);

                    String authToken = accessor.getFirstNativeHeader("x-auth-token");

                    log.info("Header auth token: " + authToken);

                    Principal principal = jwtTokenProvider.getAuthentication(authToken);
                    if (Objects.isNull(principal))
                        return null;

                    accessor.setUser(principal);
                } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                    if (Objects.nonNull(authentication))
                        log.info("Disconnected Auth : " + authentication.getName());
                    else
                        log.info("Disconnected Sess : " + accessor.getSessionId());
                }
                return message;
            }

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
                StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

                // ignore non-STOMP messages like heartbeat messages
                if (sha.getCommand() == null) {
                    log.warn("postSend null command");
                    return;
                }

                String sessionId = sha.getSessionId();

                switch (sha.getCommand()) {
                    case CONNECT:
                        log.info("STOMP Connect [sessionId: " + sessionId + "]");
                        break;
                    case CONNECTED:
                        log.info("STOMP Connected [sessionId: " + sessionId + "]");
                        break;
                    case DISCONNECT:
                        log.info("STOMP Disconnect [sessionId: " + sessionId + "]");
                        break;
                    default:
                        break;

                }
            }
        });

    }

}

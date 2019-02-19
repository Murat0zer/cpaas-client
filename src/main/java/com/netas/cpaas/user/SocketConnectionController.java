package com.netas.cpaas.user;

import com.netas.cpaas.SpringContext;
import com.netas.cpaas.chat.ChatService;
import com.netas.cpaas.chat.model.message.ChatMessage;
import com.netas.cpaas.chat.model.message.ChatMessageJson;
import com.netas.cpaas.security.JwtTokenProvider;
import com.netas.cpaas.user.model.User;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedList;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SocketConnectionController {

    private final NvsUserService nvsUserService = SpringContext.getBean(NvsUserService.class);

    private final ChatService chatService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/{sender}/chat/{receiver}")
    public void test (@DestinationVariable String sender,
                                 @DestinationVariable String receiver,
                                 @Payload ChatMessageJson chatMessageJson,
                                 Principal principal) {

        SecurityContextHolder.getContext().setAuthentication((Authentication) principal);
        log.info(chatMessageJson.chatMessage.getText());
        String domain = "@nts.ipa4.att.com";
        String receiverAddress = receiver + domain;
        chatService.sendMessage(nvsUserService.getNvsUserId(), receiverAddress, chatMessageJson);
    }
}

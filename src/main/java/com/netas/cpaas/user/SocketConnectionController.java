package com.netas.cpaas.user;

import com.netas.cpaas.chat.ChatService;
import com.netas.cpaas.chat.model.message.ChatMessage;
import com.netas.cpaas.chat.model.message.ChatMessageJson;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SocketConnectionController {

    private final NvsUserService nvsUserService;

    private final ChatService chatService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    @SendTo("/notifications")
    public ChatMessageJson test (ChatMessageJson chatMessageJson) {

        log.info(chatMessageJson.chatMessage.getText());
//        chatService.sendMessage(nvsUserService.getNvsUserId(), "user2@nts.ipa4.att.com", (ChatMessageJson) message);
        return chatMessageJson;
    }
}

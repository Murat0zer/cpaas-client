package com.netas.cpaas.user;

import com.netas.cpaas.chat.ChatService;
import com.netas.cpaas.chat.model.message.ChatMessageJson;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SocketConnectionController {

    private final NvsUserService nvsUserService;

    private final ChatService chatService;

    @MessageMapping("/chat")
    @SendTo("/notifications")
    public ResponseEntity<Object> test(@RequestBody ChatMessageJson chatMessageJson) {

        chatService.sendMessage(nvsUserService.getNvsUserId(), "user2@nts.ipa4.att.com", chatMessageJson);
        return ResponseEntity.ok(chatMessageJson);
    }
}

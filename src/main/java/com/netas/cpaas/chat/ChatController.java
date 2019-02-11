package com.netas.cpaas.chat;

import com.netas.cpaas.CustomException;
import com.netas.cpaas.chat.model.ChatMessage;
import com.netas.cpaas.chat.model.ChatMessageNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chatMessageNotification")
    public void chatNotification(@RequestBody ChatMessageNotification chatMessageNotification) {

         log.info(chatMessageNotification.toString());

    }

    @PostMapping("/{userId}/oneToOne/{otherUserId}")
    public ResponseEntity<Object> sendChatMessage(@PathVariable String userId,
                                                  @PathVariable String otherUserId,
                                                  @RequestBody ChatMessage chatMessage) {

        try {
            chatService.sendMessage(userId, otherUserId, chatMessage);
        } catch (CustomException e) {

        }

        return null;
    }
}

package com.netas.cpaas.chat;

import com.netas.cpaas.CustomException;
import com.netas.cpaas.chat.model.ChatMessage;
import com.netas.cpaas.chat.model.ChatMessageNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chatMessageNotification")
    public ResponseEntity<Object> chatNotification(@RequestBody ChatMessageNotification chatMessageNotification) {

        return null;
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

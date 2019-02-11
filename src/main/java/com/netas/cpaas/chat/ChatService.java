package com.netas.cpaas.chat;

import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.NvsProjectProperties;
import com.netas.cpaas.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class ChatService {

    String baseUrl = "https://nvs-cpaas-oauth.kandy.io/cpaas/chat/v1";

    private final HazelCastMapProvider hazelCastMapProvider;

    private final NvsProjectProperties nvsProjectProperties;

    private final RestTemplate restTemplate;


    public ChatMessage sendMessage(String userId, String otherUserId, ChatMessage chatMessage) {

        String url = baseUrl + "/" + userId + "/oneToOne/" + otherUserId + "/adhoc/messages";

        HttpHeaders httpHeaders = new HttpHeaders();

        Object token = hazelCastMapProvider.getValue(HazelCastMapProvider.getApplicationTokenMapName(), nvsProjectProperties.getProjectId());
        httpHeaders.setBearerAuth(token.toString());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatMessage> request = new HttpEntity<>(chatMessage, httpHeaders);

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, request, Object.class);
        return (ChatMessage) responseEntity.getBody();
    }

    public void subscribeChatServiceNotifications() {

    }
}

package com.netas.cpaas.chat;

import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.chat.model.ChatMessage;
import com.netas.cpaas.chat.model.notification.CallbackReference;
import com.netas.cpaas.chat.model.notification.ChatNotificationSubscription;
import com.netas.cpaas.chat.model.notification.ChatSubscriptionJson;
import com.netas.cpaas.nvs.NvsApiRequestUrl;
import com.netas.cpaas.nvs.NvsProjectProperties;
import com.netas.cpaas.user.model.NvsTokenInfo;
import com.netas.cpaas.user.service.NvsUserService;
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

    private final HazelCastMapProvider hazelCastMapProvider;

    private final NvsProjectProperties nvsProjectProperties;

    private final RestTemplate restTemplate;

    private final NvsUserService nvsUserService;


    public ChatMessage sendMessage(String userId, String otherUserId, ChatMessage chatMessage) {

        String url = NvsApiRequestUrl.getUrlForApiRequest() + "/" + userId + "/oneToOne/" + otherUserId + "/adhoc/messages";

        HttpHeaders httpHeaders = new HttpHeaders();

        String mapName = HazelCastMapProvider.MapNames.NVS_TOKEN;
        Object token = hazelCastMapProvider.getValue(mapName, nvsProjectProperties.getProjectId());
        httpHeaders.setBearerAuth(token.toString());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatMessage> request = new HttpEntity<>(chatMessage, httpHeaders);

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, request, Object.class);
        return (ChatMessage) responseEntity.getBody();
    }

    public void subscribeChatServiceNotifications() {

        String mapName = HazelCastMapProvider.MapNames.NOTIFICATION_CHANNELS;
        String nvsUserId = nvsUserService.getNvsUserId();
        String notifyURl = (String) hazelCastMapProvider.getMap(mapName).get(nvsUserId);

        CallbackReference callbackReference = CallbackReference.builder().notifyURL(notifyURl).build();
        ChatNotificationSubscription chatNotificationSubscription = ChatNotificationSubscription.builder()
                .callbackReference(callbackReference)
                .clientCorrelator(nvsUserId)
                .build();
        ChatSubscriptionJson chatSubscriptionJson = ChatSubscriptionJson.builder()
                .chatNotificationSubscription(chatNotificationSubscription)
                .build();

        NvsTokenInfo nvsTokenInfo = nvsUserService.getNvsTokenInfo();

        NvsApiRequestUrl.setApiName("chat");
        NvsApiRequestUrl.setApiVersion("v1");
        NvsApiRequestUrl.setUserId(nvsUserId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(nvsTokenInfo.getAccessToken());
        HttpEntity<ChatSubscriptionJson> httpEntity = new HttpEntity<>(chatSubscriptionJson, httpHeaders);

        String url = NvsApiRequestUrl.getUrlForApiRequest() + "/subscriptions";
        ResponseEntity<ChatSubscriptionJson> responseEntity;

        responseEntity = restTemplate.postForEntity(url, httpEntity, ChatSubscriptionJson.class);

    }
}

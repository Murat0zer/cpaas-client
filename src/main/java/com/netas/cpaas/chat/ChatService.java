package com.netas.cpaas.chat;

import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.chat.model.notification.CallbackReference;
import com.netas.cpaas.chat.model.message.ChatMessageJson;
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


    public void sendMessage(String userId, String otherUserPrimaryContact, ChatMessageJson chatMessage) {

        NvsApiRequestUrl.setUserId(userId);
        NvsApiRequestUrl.setApiVersion("v1");
        NvsApiRequestUrl.setApiName("chat");

        String url = NvsApiRequestUrl.getUrlForApiRequest() + "/oneToOne/" + otherUserPrimaryContact + "/adhoc/messages";

        HttpHeaders httpHeaders = new HttpHeaders();

        Object token = nvsUserService.getNvsTokenInfo().getAccessToken();
        httpHeaders.setBearerAuth(token.toString());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatMessageJson> request = new HttpEntity<>(chatMessage, httpHeaders);

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, request, Object.class);
        Object object = responseEntity.getBody();
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

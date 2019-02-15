package com.netas.cpaas.notification;

import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.nvs.NvsApiRequestUrl;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NvsUserService nvsUserService;

    private final RestTemplate restTemplate;

    private final HazelCastMapProvider hazelCastMapProvider;

    private SubscribeNotificationWebSocketJson getRequestBody() {

        SubscribeNotificationWebSocketJson requestBody;
        NotificationChannel notificationChannel = NotificationChannel.builder()
                .clientCorrelator("")
                .channelType("WebSockets")
                .channelLifetime(3600)
                .build();
        requestBody = SubscribeNotificationWebSocketJson.builder()
                .notificationChannel(notificationChannel)
                .build();
        return requestBody;
    }

    public void subscribeNotifications() {
        NvsApiRequestUrl.setApiName("notificationchannel");
        NvsApiRequestUrl.setApiVersion("v1");
        NvsApiRequestUrl.setUserId(nvsUserService.getNvsUserId());

        SubscribeNotificationWebSocketJson requestBody = getRequestBody();
        requestBody.getNotificationChannel().setClientCorrelator(nvsUserService.getNvsUserId());
        this.createWebSocket(requestBody);
    }

    private void createWebSocket(SubscribeNotificationWebSocketJson requestBody) {

        log.info("creating websocket for notifications");

        HttpEntity<SubscribeNotificationWebSocketJson> httpEntity;
        httpEntity = prepareRequest(requestBody);

        String url = NvsApiRequestUrl.getUrlForApiRequest() + "/channels";
        ResponseEntity<SubscribeNotificationWebSocketJson> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(url, httpEntity, SubscribeNotificationWebSocketJson.class);
        } catch (HttpClientErrorException e) {
            log.error(e.getResponseBodyAsString(), e.getStatusCode().toString());
            log.error("creating websocket for notifications has failed.");
            throw e;
        }
        requestBody = responseEntity.getBody();

        log.info("websocket for notifications has created successfully");

        String callbackURL = Objects.requireNonNull(requestBody).getNotificationChannel().getCallbackURL();
        this.establishWebSocketConnection(callbackURL);

        String mapName = HazelCastMapProvider.MapNames.NOTIFICATION_CHANNELS;
        hazelCastMapProvider.putToMap(mapName, NvsApiRequestUrl.getUserId(), callbackURL);
    }

    private void establishWebSocketConnection(String callBackUrl) {

        log.info("creating actual websocket...");
        String url = NvsApiRequestUrl.getUrlForWebSocket() + "/channels" + "/" + callBackUrl + "/websocket?access_token={keyword}";
        String uriVariableValue = nvsUserService.getNvsTokenInfo().getAccessToken();
        StandardWebSocketClient client = new StandardWebSocketClient();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> userProperties = new ConcurrentHashMap<>();
        userProperties.put("auth", authentication);
        client.setUserProperties(userProperties);

        try {
            WebSocketSession session = client.doHandshake(new WebSocketHandler(), url, uriVariableValue).get();
            HazelCastMapProvider.getNvsWebSocketMap().put(nvsUserService.getNvsUserId(), session);
        } catch (Exception e) {
            log.error("Error while creating websocket !!");
            log.info(e.getMessage());
        }

    }

    private <T> HttpEntity<T> prepareRequest(Object requestBody) {

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBearerAuth(nvsUserService.getNvsTokenInfo().getAccessToken());

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>((T) requestBody, httpHeaders);
    }
}
package com.netas.cpaas.notification;

import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.nvs.NvsApiRequestUrl;
import com.netas.cpaas.nvs.NvsProjectProperties;
import com.netas.cpaas.user.NvsUserUtils;
import com.netas.cpaas.user.model.NvsTokenInfo;
import com.netas.cpaas.user.model.User;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NvsUserService nvsUserService;

    private final NvsProjectProperties nvsProjectProperties;

    private final RestTemplate restTemplate;

    private NvsTokenInfo nvsTokenInfo;

    private final HazelCastMapProvider hazelCastMapProvider;

    private SubscribeNotificationWebSocketJson requestBody;

    @PostConstruct
    public void init() {

        NotificationChannel notificationChannel = NotificationChannel.builder()
                .clientCorrelator(nvsProjectProperties.getProjectId())
                .channelType("WebSockets")
                .xConnCheckRole("server")
                .channelLifetime(3600)
                .build();
        requestBody = SubscribeNotificationWebSocketJson.builder()
                .notificationChannel(notificationChannel)
                .build();

    }

    public void subscribeNotifications() {
        NvsApiRequestUrl.setApiName("notificationchannel");
        NvsApiRequestUrl.setApiVersion("v1");
        NvsApiRequestUrl.setUserId(nvsUserService.getNvsUserId());

        this.createWebSocket();
    }
    private void createWebSocket() {

        log.error("creating websocket for notifications");

        HttpEntity<SubscribeNotificationWebSocketJson> httpEntity;
        httpEntity = prepareRequest(requestBody);

        String url = NvsApiRequestUrl.getUrlForApiRequest() + "/channels";
        ResponseEntity<SubscribeNotificationWebSocketJson> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(url, httpEntity, SubscribeNotificationWebSocketJson.class);
        } catch (HttpClientErrorException e) {
            log.error(e.getResponseBodyAsString(),  e.getStatusCode().toString());
            log.error("creating websocket for notifications has failed.");
            throw e;
        }
        requestBody = responseEntity.getBody();

        log.info("websocket for notifications has created successfully");

        this.establishWebSocketConnection(Objects.requireNonNull(requestBody).notificationChannel.callbackURL);

        String mapName = HazelCastMapProvider.MapNames.NOTIFICATION_CHANNELS;
        hazelCastMapProvider.putToMap(mapName, NvsApiRequestUrl.getUserId(), requestBody.notificationChannel.callbackURL );
    }

    private void establishWebSocketConnection(String callBackUrl) {

        log.info("creating actual websocket...");

        String url = NvsApiRequestUrl.getUrlForWebSocket() + "/channels" + "/" + callBackUrl + "/websocket?access_token={keyword}";
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new NotificationStompSessionHandler();

        String uriVariableValue = nvsTokenInfo.getAccessToken();

        try {
            stompClient.connect(url, sessionHandler, uriVariableValue);
        } catch (Exception e) {
            log.error("failed to create websocket !");
            log.error(e.getMessage());
        }
        log.info("Websocket has created. =" + stompClient.toString());
    }

    private <T> HttpEntity<T> prepareRequest(SubscribeNotificationWebSocketJson requestBody) {

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBearerAuth(nvsTokenInfo.getAccessToken());

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>((T) requestBody, httpHeaders);
    }






//    public void createWebHook(WebHook webHook) {
//        log.info("creating webhook for notifications");
//        String baseUrl = "https://nvs-cpaas-oauth.kandy.io/cpaas/notificationchannel/v1/";
//
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//
//        NvsTokenInfo nvsTokenInfo = (NvsTokenInfo) hazelCastMapProvider
//                .getMap(HazelCastMapProvider.getNvsTokenMapName()).get(nvsProjectProperties.getProjectId());
//
//        httpHeaders.setBearerAuth(nvsTokenInfo.getAccessToken());
//
//
//        String idToken = ((NvsTokenInfo) hazelCastMapProvider.getMap(HazelCastMapProvider.getNvsTokenMapName())
//                .get(nvsProjectProperties.getProjectId())).getIdToken();
//
//        String cpaasUserId = NvsUserUtils.getNvsUserInfoFromIdToken(idToken).getPreferredUsername();
//        String url = baseUrl + cpaasUserId + "/channels";
//
//
//        HttpEntity<WebHook> httpEntity = new HttpEntity<>(webHook, httpHeaders);
//
//        ResponseEntity<WebHook> responseEntity;
//        try {
//            responseEntity = restTemplate.postForEntity(url, httpEntity, WebHook.class);
//        } catch (HttpClientErrorException e) {
//            log.error(e.getResponseBodyAsString(),  e.getStatusCode().toString());
//            log.error("creating webhook has failed.");
//            throw e;
//        }
//        webHook = responseEntity.getBody();
//
//        log.info("webhook created successfully");
//
//    }


}

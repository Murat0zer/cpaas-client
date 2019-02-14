package com.netas.cpaas.notification;

import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.nvs.NvsApiRequestUrl;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.util.Objects;

@EnableWebSocket
@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NvsUserService nvsUserService;

    private final RestTemplate restTemplate;

    private final HazelCastMapProvider hazelCastMapProvider;

    private SubscribeNotificationWebSocketJson requestBody;

    @PostConstruct
    public void init() {

        NotificationChannel notificationChannel = NotificationChannel.builder()
                .clientCorrelator("")
                .channelType("WebSockets")
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

        log.info("creating websocket for notifications");

        requestBody.notificationChannel.clientCorrelator = nvsUserService.getNvsUserId();
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

        this.establishWebSocketConnection(Objects.requireNonNull(requestBody).notificationChannel.callbackURL);

        String mapName = HazelCastMapProvider.MapNames.NOTIFICATION_CHANNELS;
        hazelCastMapProvider.putToMap(mapName, NvsApiRequestUrl.getUserId(), requestBody.notificationChannel.callbackURL);
    }

    private void establishWebSocketConnection(String callBackUrl) {

        log.info("creating actual websocket...");

        String url = NvsApiRequestUrl.getUrlForWebSocket() + "/channels" + "/" + callBackUrl + "/websocket?access_token={keyword}";
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new StringMessageConverter());

        String uriVariableValue = nvsUserService.getNvsTokenInfo().getAccessToken();

        WebSocketClient client = new StandardWebSocketClient();
        try {
            WebSocketSession session = client.doHandshake(new SocketHandler(), url, uriVariableValue).get();
            session.sendMessage(new TextMessage("Hello World test."));
        } catch (Exception e) {
            log.info(e.getMessage());
        }

//        try {
//            stompClient.connect(url, new NotificationStompSessionHandler(), uriVariableValue);
//        } catch (Exception e) {
//            log.error("failed to create websocket !");
//            log.error(e.getMessage());
//        }
//        log.info("Websocket has created. =" + stompClient.toString());

    }

    private <T> HttpEntity<T> prepareRequest(SubscribeNotificationWebSocketJson requestBody) {

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBearerAuth(nvsUserService.getNvsTokenInfo().getAccessToken());

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>((T) requestBody, httpHeaders);
    }

    @EventListener(SessionConnectedEvent.class)
    public void sessionConnectedEvent(SessionConnectedEvent event) {
        log.info("asdasdas");
    }

    @EventListener(SessionConnectEvent.class)
    public void sessionConnectEvent(SessionConnectEvent event) {
        log.info("asdasdas");
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
//        String cpaasUserId = NvsUserUtils.getNvsUserFromIdToken(idToken).getPreferredUsername();
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

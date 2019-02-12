package com.netas.cpaas.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.NvsProjectProperties;
import com.netas.cpaas.user.NvsUserUtils;
import com.netas.cpaas.user.model.NvsTokenInfo;
import com.netas.cpaas.user.model.NvsUserInfo;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final HazelCastMapProvider hazelCastMapProvider;

    private final NvsProjectProperties nvsProjectProperties;

    private final RestTemplate restTemplate;

    public void createWebHook(WebHook webHook) {
        log.info("creating webhook for notifications");
        String baseUrl = "https://nvs-cpaas-oauth.kandy.io/cpaas/notificationchannel/v1/";


        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        NvsTokenInfo nvsTokenInfo = (NvsTokenInfo) hazelCastMapProvider
                .getMap(HazelCastMapProvider.getNvsTokenMapName()).get(nvsProjectProperties.getProjectId());

        httpHeaders.setBearerAuth(nvsTokenInfo.getAccessToken());


        String idToken = ((NvsTokenInfo) hazelCastMapProvider.getMap(HazelCastMapProvider.getNvsTokenMapName())
                .get(nvsProjectProperties.getProjectId())).getIdToken();

        String cpaasUserId = NvsUserUtils.getNvsUserInfoFromIdToken(idToken).getPreferredUsername();
        String url = baseUrl + cpaasUserId + "/channels";


        HttpEntity<WebHook> httpEntity = new HttpEntity<>(webHook, httpHeaders);

        ResponseEntity<WebHook> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(url, httpEntity, WebHook.class);
        } catch (HttpClientErrorException e) {
            log.error(e.getResponseBodyAsString(),  e.getStatusCode().toString());
            log.error("creating webhook has failed.");
            throw e;
        }
        webHook = responseEntity.getBody();

        log.info("webhook created successfully");


    }
}

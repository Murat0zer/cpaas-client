package com.netas.cpaas.notification;

import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.NvsProjectProperties;
import com.netas.cpaas.config.JwtConfig;
import com.netas.cpaas.user.model.NvsTokenInfo;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final HazelCastMapProvider hazelCastMapProvider;

    private final NvsProjectProperties nvsProjectProperties;

    private final JwtConfig jwtConfig;

    private final RestTemplate restTemplate;

    public void createWebHook(WebHook webHook) {

        String baseUrl = "https://nvs-cpaas-oauth.kandy.io/cpaas/notificationchannel/v1/";


        HttpHeaders httpHeaders = new HttpHeaders();

        NvsTokenInfo nvsTokenInfo = (NvsTokenInfo) hazelCastMapProvider
                .getMap(HazelCastMapProvider.getNvsTokenMapName()).get(nvsProjectProperties.getProjectId());

        httpHeaders.setBearerAuth(nvsTokenInfo.getAccessToken());

        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();

        // TODO: 11.02.2019 Userinfo alip id yi gonder burdan falan

        String url = baseUrl + appUserId + "/channels";


        HttpEntity<WebHook> httpEntity = new HttpEntity<>(webHook, httpHeaders);

        ResponseEntity<WebHook> responseEntity;
        responseEntity = restTemplate.postForEntity(url, httpEntity, WebHook.class);

        webHook = responseEntity.getBody();
    }
}

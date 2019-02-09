package com.netas.cpaas.user.service;

import com.netas.cpaas.user.model.NvsLoginDto;
import com.netas.cpaas.user.model.NvsTokenInfo;
import com.netas.cpaas.user.model.NvsUser;
import com.netas.cpaas.user.model.register.NvsRegisterDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@AllArgsConstructor
public class NvsUserService {

    @Value("${api.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    NvsTokenInfo authUserForNvs(NvsLoginDto nvsLoginDto) {

        String url = baseUrl + "/auth/v1/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", nvsLoginDto.getEmail());
        map.add("password", nvsLoginDto.getPassword());
        map.add("grant_type", nvsLoginDto.getGrantType());
        map.add("client_id", nvsLoginDto.getClientId());
        map.add("scope", nvsLoginDto.getScope());

        HttpEntity<NvsLoginDto> request = new HttpEntity<>(nvsLoginDto, headers);
        ResponseEntity<NvsTokenInfo> responseEntity;

        responseEntity = restTemplate.postForEntity(url, request, NvsTokenInfo.class);


        return responseEntity.getBody();
    }

    public NvsUser createUser(NvsRegisterDto nvsRegisterDto) {

        String url = "https://nvs-apimarket.kandy.io/api/graphql";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NvsRegisterDto> request = new HttpEntity<>(nvsRegisterDto, headers);

        ResponseEntity<NvsUser> responseEntity;
        responseEntity = restTemplate.postForEntity(url, request, NvsUser.class);
        return  responseEntity.getBody();

    }
}

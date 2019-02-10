package com.netas.cpaas.user.service;

import com.netas.cpaas.user.model.create.CreatedUserDto;
import com.netas.cpaas.user.model.NvsLoginDto;
import com.netas.cpaas.user.model.NvsTokenInfo;
import com.netas.cpaas.user.model.NvsUser;
import com.netas.cpaas.user.model.register.NvsRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NvsUserService {

    private final RestTemplate restTemplate;
    @Value("${api.base.url}")
    private String baseUrl;

    public NvsTokenInfo authUserForNvs(NvsLoginDto nvsLoginDto) {

        String url = baseUrl + "/auth/v1/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", nvsLoginDto.getEmail());
        map.add("password", nvsLoginDto.getPassword());
        map.add("grant_type", nvsLoginDto.getGrantType());
        map.add("client_id", nvsLoginDto.getClientId());
        map.add("scope", nvsLoginDto.getScope());

        HttpEntity<MultiValueMap> request = new HttpEntity<>(map, headers);
        ResponseEntity<NvsTokenInfo> responseEntity;

        responseEntity = restTemplate.postForEntity(url, request, NvsTokenInfo.class);

        return responseEntity.getBody();
    }

    public NvsUser createUser(NvsRegisterDto nvsRegisterDto) {

        String url = "https://nvs-apimarket.kandy.io/api/graphql";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBasicAuth("kandyadmin", "mK3!u2PI*@Buas#@4L19"); // FIXME: 10.02.2019 !hardcoded

        headers.add("X-Token", nvsRegisterDto.getXToken());

        HttpEntity<NvsRegisterDto> request = new HttpEntity<>(nvsRegisterDto, headers);

        ResponseEntity<CreatedUserDto> responseEntity;
        responseEntity = restTemplate.postForEntity(url, request, CreatedUserDto.class);

        return  Objects.requireNonNull(responseEntity.getBody()).getDataOfCreatedUser().getNvsUser();

    }
}

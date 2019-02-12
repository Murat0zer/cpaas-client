package com.netas.cpaas.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.netas.cpaas.CustomException;
import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.NvsProjectProperties;
import com.netas.cpaas.user.model.NvsLoginDto;
import com.netas.cpaas.user.model.NvsTokenInfo;
import com.netas.cpaas.user.model.NvsUser;
import com.netas.cpaas.user.model.NvsUserInfo;
import com.netas.cpaas.user.model.nvs.NvsGraphqlDto;
import com.netas.cpaas.user.model.nvs.create.NvsCreatedUser;
import com.netas.cpaas.user.model.register.RegistrationDto;
import com.netas.cpaas.user.model.register.Variables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.netas.cpaas.HazelCastMapProvider.getNvsTokenMapName;

@Slf4j
@Service
@RequiredArgsConstructor
public class NvsUserService {

    private final RestTemplate restTemplate;

    private final HazelCastMapProvider hazelCastMapProvider;

    private final NvsProjectProperties nvsProjectProperties;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    @Value("${project.portal.url}")
    private String projectPortalUrl;


    public NvsTokenInfo authUserForNvs(NvsLoginDto nvsLoginDto) {

        HttpHeaders headers = new HttpHeaders();

        String url = apiBaseUrl + "/auth/v1/token";

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

    NvsUser createUser(RegistrationDto registrationDto) {

        HttpHeaders headers = new HttpHeaders();

        this.authenticateRequest(headers, nvsProjectProperties.getProjectId());

        headers.setContentType(MediaType.APPLICATION_JSON);

        // TODO: 10.02.2019 grapql kullan ?
        NvsGraphqlDto nvsGraphqlDto = NvsGraphqlDto.builder()
                .operationName("CreateUserMutation")
                .query("mutation CreateUserMutation($input: CreateUserInput!) { " +
                        "createUser(input: $input) { ...AccountsPage_users __typename } } " +
                        "fragment AccountsPage_users on User { " +
                        "firstName lastName userName email id createdOn status roles __typename }")
                .variables(Variables.builder().input(registrationDto).build())
                .build();

        HttpEntity<NvsGraphqlDto> request = new HttpEntity<>(nvsGraphqlDto, headers);

        ResponseEntity<NvsCreatedUser> responseEntity;
        responseEntity = restTemplate.postForEntity(projectPortalUrl, request, NvsCreatedUser.class);

        return Objects.requireNonNull(responseEntity.getBody()).getDataOfCreatedNvsUser().getNvsUser();
    }

    void deleteUser(String nvsId) {

        HttpHeaders headers = new HttpHeaders();

        this.authenticateRequest(headers, nvsProjectProperties.getProjectId());

        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, List<String>> ids = new HashMap<>();
        ids.put("ids", Lists.newArrayList(Collections.singletonList(nvsId)));
        NvsGraphqlDto nvsGraphqlDto = NvsGraphqlDto.builder()
                .operationName("DestroyUsersMutation")
                .query("mutation DestroyUsersMutation($input: DestroyUsersInput!) {  destroyUsers(input: $input) {    id    __typename  }}")
                .variables(Variables.builder().input(ids).build())
                .build();

        HttpEntity<NvsGraphqlDto> request = new HttpEntity<>(nvsGraphqlDto, headers);
        try {
            restTemplate.postForEntity(projectPortalUrl, request, Object.class);
        } catch (HttpClientErrorException e) {
            String message = e.getResponseBodyAsString();
            HttpStatus httpStatus = e.getStatusCode();
            throw new CustomException(message, httpStatus);
        }

    }

    private void authenticateRequest(HttpHeaders headers, String username) {

        NvsTokenInfo nvsTokenInfo = (NvsTokenInfo) hazelCastMapProvider.getValue(getNvsTokenMapName(), username);

        headers.add("X-Token", nvsTokenInfo.getAccessToken());
        headers.setBasicAuth("kandyadmin", "mK3!u2PI*@Buas#@4L19"); // FIXME: 10.02.2019 !hardcoded
    }
}

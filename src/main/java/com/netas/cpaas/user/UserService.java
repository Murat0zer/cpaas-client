package com.netas.cpaas.user;

import com.netas.cpaas.CustomException;
import com.netas.cpaas.config.JwtTokenProvider;
import com.netas.cpaas.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class UserService {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String baseUrl;

    @Autowired
    public UserService(UserDetailsServiceImpl userDetailsService, JwtTokenProvider jwtTokenProvider,
                       AuthenticationManagerBuilder authenticationManagerBuilder, RestTemplate restTemplate) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.restTemplate = restTemplate;
    }

    public void signin(String username, String password) {
        try {
            AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            Set<Role> roles = (Set<Role>) userDetailsService.loadUserByUsername(username).getAuthorities();
            String token = jwtTokenProvider.createToken(username, roles);

            User user = User.builder().build();

            if (authentication.getPrincipal() instanceof UserDetails)
                user = ((User) authentication.getPrincipal());

            user.setToken(token);

            NvsLoginDto nvsLoginDto = NvsLoginDto.builder()
                    .clientId(String.valueOf(user.getId()))
                    .grantType("password")
                    .password("97433282-4701-4ea4-a792-04b5a8d1f020")
                    .scope("openid")
                    .username("PRIV-cpaas").build();

            NvsUser nvsUser = NvsUser.builder()
                    .nvsEmail(user.getEmail())
                    .nvsPassword(user.getPassword())
                    .nvsUsername(user.getUsername())
                    .nvsTokens(authUserForNvs(nvsLoginDto))
                    .build();

            user.setNvsUser(nvsUser);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException | HttpClientErrorException e) {
            String message = e.getMessage();
            HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            if (e instanceof HttpClientErrorException) {
                message = ((HttpClientErrorException) e).getResponseBodyAsString();
                httpStatus = ((HttpClientErrorException) e).getStatusCode();
            }
            throw new CustomException(message, httpStatus);
        }
    }

    private NvsTokens authUserForNvs(NvsLoginDto nvsLoginDto) {

        String url = baseUrl + "/auth/v1/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", nvsLoginDto.getUsername());
        map.add("password", nvsLoginDto.getPassword());
        map.add("grant_type", nvsLoginDto.getGrantType());
        map.add("client_id", nvsLoginDto.getClientId());
        map.add("scope", nvsLoginDto.getScope());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<NvsTokens> responseEntity;

        responseEntity = restTemplate.postForEntity(url, request, NvsTokens.class);


        return responseEntity.getBody();
    }
}

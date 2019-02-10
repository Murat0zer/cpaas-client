package com.netas.cpaas.user.service;

import com.google.common.collect.Sets;
import com.netas.cpaas.CustomException;
import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.NvsProjectProperties;
import com.netas.cpaas.config.JwtTokenProvider;
import com.netas.cpaas.user.UserRepository;
import com.netas.cpaas.user.model.*;
import com.netas.cpaas.user.model.register.NvsRegisterDto;
import com.netas.cpaas.user.model.register.RegistrationDto;
import com.netas.cpaas.user.model.register.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

import static com.netas.cpaas.HazelCastMapProvider.getNvsTokenMapName;

@Service
public class UserService {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final PasswordEncoder passwordEncoder;

    private final NvsUserService nvsUserService;

    private final UserRepository userRepository;

    private final HazelCastMapProvider hazelCastMapProvider;

    private final NvsProjectProperties nvsProjectProperties;

    @Autowired
    public UserService(UserDetailsServiceImpl userDetailsService,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManagerBuilder authenticationManagerBuilder,
                       PasswordEncoder passwordEncoder,
                       NvsUserService nvsUserService,
                       UserRepository userRepository,
                       HazelCastMapProvider hazelCastMapProvider,
                       NvsProjectProperties nvsProjectProperties) {

        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
        this.nvsUserService = nvsUserService;
        this.userRepository = userRepository;
        this.hazelCastMapProvider = hazelCastMapProvider;
        this.nvsProjectProperties = nvsProjectProperties;
    }

    public void signin(String username, String password) {
        try {
            AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            @SuppressWarnings("unchecked")
            Set<Role> roles = (Set<Role>) userDetailsService.loadUserByUsername(username).getAuthorities();
            String token = jwtTokenProvider.createToken(username, roles);

            User user = User.builder().build();

            if (authentication.getPrincipal() instanceof UserDetails)
                user = ((User) authentication.getPrincipal());

            user.setToken(token);

            NvsLoginDto nvsLoginDto = NvsLoginDto.builder()
                    .clientId(String.valueOf(user.getId()))
                    .grantType("password")
                    .password(user.getPassword())
                    .scope("openid")
                    .email(user.getEmail()).build();

            user.getNvsUser().setNvsTokenInfo(nvsUserService.authUserForNvs(nvsLoginDto));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (AuthenticationException e) {
            String message = e.getMessage();
            HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            throw new CustomException(message, httpStatus);

        } catch (HttpClientErrorException e) {
            String message = e.getResponseBodyAsString();
            HttpStatus httpStatus = e.getStatusCode();
            throw new CustomException(message, httpStatus);
        }
    }

    public User signup(RegistrationDto registrationDto) {

        User user = User.builder()
                .userAddress(registrationDto.getUserAddress())
                .email(registrationDto.getEmail())
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .serviceAddress(registrationDto.getServiceAddress())
                .nvsUser(NvsUser.builder().build())
                .roles(Sets.newHashSet(Role.USER))
                .username(registrationDto.getUserName())
                .build();

        // TODO: 10.02.2019 grapql kullan ?

        NvsTokenInfo appNvsTokenInfo;
        appNvsTokenInfo = (NvsTokenInfo) hazelCastMapProvider.getValue(getNvsTokenMapName(), nvsProjectProperties.getProjectId());
        NvsRegisterDto nvsRegisterDto = NvsRegisterDto.builder()
                .operationName("CreateUserMutation")
                .query("mutation CreateUserMutation($input: CreateUserInput!) { " +
                        "createUser(input: $input) { ...AccountsPage_users __typename } } " +
                        "fragment AccountsPage_users on User { " +
                        "firstName lastName userName email id createdOn status roles __typename }")
                .variables(Variables.builder().input(registrationDto).build())
                .xToken(appNvsTokenInfo.getAccessToken())
                .build();
        try {
            user.setNvsUser(nvsUserService.createUser(nvsRegisterDto));
        } catch (HttpClientErrorException e) {
            throw new CustomException(e.getResponseBodyAsString(), e.getStatusCode());
        }
        userRepository.save(user);
        return user;
    }

}

package com.netas.cpaas.user.service;

import com.google.common.collect.Sets;
import com.hazelcast.core.HazelcastInstance;
import com.netas.cpaas.CustomException;
import com.netas.cpaas.config.JwtTokenProvider;
import com.netas.cpaas.user.UserRepository;
import com.netas.cpaas.user.model.*;
import com.netas.cpaas.user.model.register.NvsRegisterDto;
import com.netas.cpaas.user.model.register.RegistrationDto;
import com.netas.cpaas.user.model.register.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
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

@Service
public class UserService {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final PasswordEncoder passwordEncoder;

    private final NvsUserService nvsUserService;

    private final UserRepository userRepository;

    private final HazelcastInstance hazelcastInstance;

    @Autowired
    public UserService(UserDetailsServiceImpl userDetailsService,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManagerBuilder authenticationManagerBuilder,
                       PasswordEncoder passwordEncoder,
                       NvsUserService nvsUserService,
                       UserRepository userRepository,
                       @Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {

        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
        this.nvsUserService = nvsUserService;
        this.userRepository = userRepository;
        this.hazelcastInstance = hazelcastInstance;
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
                    .password(user.getPassword())
                    .scope("openid")
                    .email(user.getEmail()).build();

            user.getNvsUser().setNvsTokenInfo(nvsUserService.authUserForNvs(nvsLoginDto));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (AuthenticationException e ) {
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
                .build();

        // TODO: 10.02.2019 grapql kullan ?
        NvsRegisterDto nvsRegisterDto =  NvsRegisterDto.builder()
                .operationName("CreateUserMutation")
                .query("mutation CreateUserMutation($input: CreateUserInput!) { " +
                        "createUser(input: $input) { ...AccountsPage_users __typename } } " +
                        "fragment AccountsPage_users on User { " +
                        "firstName lastName userName email id createdOn status roles __typename }")
                .variables(Variables.builder().input(registrationDto).build())
                .xToken(hazelcastInstance.getMap("app").get("access-token").toString())
                .build();
        user.setNvsUser(nvsUserService.createUser(nvsRegisterDto));
        userRepository.save(user);
        return user;
    }

}

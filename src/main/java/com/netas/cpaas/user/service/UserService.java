package com.netas.cpaas.user.service;

import com.google.common.collect.Sets;
import com.netas.cpaas.CustomException;
import com.netas.cpaas.HazelCastMapProvider;
import com.netas.cpaas.chat.ChatService;
import com.netas.cpaas.notification.NotificationService;
import com.netas.cpaas.security.JwtTokenProvider;
import com.netas.cpaas.user.UserRepository;
import com.netas.cpaas.user.model.*;
import com.netas.cpaas.user.model.register.RegistrationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final PasswordEncoder passwordEncoder;

    private final NvsUserService nvsUserService;

    private final UserRepository userRepository;

    private final HazelCastMapProvider hazelCastMapProvider;

    private final ChatService chatService;

    private final NotificationService notificationService;


    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

    }

    public void signin(String username, String password) {
        try {
            AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            @SuppressWarnings("unchecked")
            Set<Role> roles = (Set<Role>) this.loadUserByUsername(username).getAuthorities();
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

            NvsTokenInfo nvsTokenInfo = nvsUserService.authUserForNvs(nvsLoginDto);
            user.getNvsUser().setNvsTokenInfo(nvsTokenInfo);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            hazelCastMapProvider.putToMap(HazelCastMapProvider.getNvsTokenMapName(), username, nvsTokenInfo);


        } catch (AuthenticationException e) {
            String message = e.getMessage();
            HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            throw new CustomException(message, httpStatus);

        } catch (HttpClientErrorException e) {
            String message = e.getResponseBodyAsString();
            HttpStatus httpStatus = e.getStatusCode();
            throw new CustomException(message, httpStatus);
        }

        notificationService.subscribeNotifications();
        chatService.subscribeChatServiceNotifications();
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
        try {
            user.setNvsUser(nvsUserService.createUser(registrationDto));
        } catch (HttpClientErrorException e) {
            throw new CustomException(e.getResponseBodyAsString(), e.getStatusCode());
        }
        try {
            userRepository.save(user);
        } catch (Exception e) {
            nvsUserService.deleteUser(user.getNvsUser().getNvsId());
            throw new CustomException("Failed. Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return user;
    }
}

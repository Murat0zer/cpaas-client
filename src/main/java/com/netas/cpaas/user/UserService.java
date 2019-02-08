package com.netas.cpaas.user;

import com.netas.cpaas.CustomException;
import com.netas.cpaas.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public UserService(UserDetailsServiceImpl userDetailsService, JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }


    public void signin(String username, String password) {
        try {
            AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtTokenProvider.createToken(username, (Set<Role>) userDetailsService.loadUserByUsername(username).getAuthorities());

            if (authentication.getPrincipal() instanceof UserDetails)
                ((User) authentication.getPrincipal()).setToken(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

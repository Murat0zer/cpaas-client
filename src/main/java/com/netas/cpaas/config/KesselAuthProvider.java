package com.netas.cpaas.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class KesselAuthProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();



        // use the credentials
        // and authenticate against the third-party system
        return new UsernamePasswordAuthenticationToken(
                name, password, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}

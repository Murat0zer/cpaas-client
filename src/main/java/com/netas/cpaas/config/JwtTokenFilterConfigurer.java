package com.netas.cpaas.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtTokenProvider jwtTokenProvider;

    private JwtConfig jwtConfig;

    public JwtTokenFilterConfigurer(JwtTokenProvider jwtTokenProvider, JwtConfig jwtConfig) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenAuthenticationFilter customFilter = new JwtTokenAuthenticationFilter(jwtConfig, jwtTokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}

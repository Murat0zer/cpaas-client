package com.netas.cpaas.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource("classpath:jwt-config.properties")
@ConfigurationProperties(prefix = "jwt.token")
public class JwtConfig {

    private String header;

    private String prefix;

    private int expiration;

    private String secret;

}

package com.netas.cpaas.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@PropertySource("classpath:jwt-config.yaml")
@ConfigurationProperties(prefix = "token")
public class JwtConfig {

    private String header;

    private String prefix;

    private int expiration;

    private String secret;

}

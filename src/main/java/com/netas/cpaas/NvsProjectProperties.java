package com.netas.cpaas;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource(value = "classpath:nvs-project-info.properties")
@ConfigurationProperties(prefix = "project.cpaas")
public class NvsProjectProperties {

    private String username;

    private String password;

    private String clientId;

    private String projectId;


}

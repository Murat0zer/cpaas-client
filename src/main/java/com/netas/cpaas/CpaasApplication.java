package com.netas.cpaas;

import com.google.common.collect.Sets;
import com.netas.cpaas.notification.NotificationService;
import com.netas.cpaas.nvs.NvsProjectProperties;
import com.netas.cpaas.user.NvsUserUtils;
import com.netas.cpaas.user.RoleRepository;
import com.netas.cpaas.user.UserRepository;
import com.netas.cpaas.user.model.*;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.netas.cpaas"})
public class CpaasApplication {

    private final HazelCastMapProvider hazelcastInstance;

    private final HazelCastMapProvider hazelCastMapProvider;

    private final NvsProjectProperties nvsProjectProperties;

    public static void main(String[] args) {
        SpringApplication.run(CpaasApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        NvsUserService nvsUserService,
                                        PasswordEncoder passwordEncoder) {
        return args -> {

            NvsLoginDto nvsLoginDto = NvsLoginDto.builder()
                    .clientId(nvsProjectProperties.getClientId())
                    .email(nvsProjectProperties.getUsername())
                    .password(nvsProjectProperties.getPassword())
                    .grantType("password")
                    .scope("openid")
                    .build();
            NvsTokenInfo projectTokenInfo;
            try {
                projectTokenInfo = nvsUserService.authUserForNvs(nvsLoginDto);
            } catch (HttpClientErrorException e) {
                log.error(e.getResponseBodyAsString());
                return;
            }

            hazelcastInstance.getMap("nvsTokenMap").put(nvsProjectProperties.getProjectId(), projectTokenInfo);

            if (roleRepository.findAll().isEmpty()) {
                roleRepository.save(Role.ADMIN);
                roleRepository.save(Role.USER);
            }

            NvsUserInfo nvsUserInfo = NvsUserInfo.builder()
                    .nvsId("")
                    .status("active")
                    .createdOn("null")
                    .typename("User")
                    .nvsRoles(Sets.newHashSet(Role.USER))
                    .build();

            UserAddress userAddress = UserAddress.builder()
                    .addressLine1("1600 Amphitheatre Pkwy")
                    .addressLine2("null")
                    .city("Mountain View")
                    .country("United States")
                    .postalCode("94043")
                    .state("CA")
                    .build();
            ServiceAddress serviceAddress = ServiceAddress.builder()
                    .serviceAddressLine1("1600 Amphitheatre Pkwy")
                    .serviceAddressLine2("null")
                    .serviceCity("Mountain View")
                    .serviceCountry("United States")
                    .servicePostalCode("94043")
                    .serviceState("CA")
                    .build();

            for (int i = 1; i <= 3; i++) {

                User user = User.builder().
                        email("user" + i + "@mailprotech.com")
                        .firstName("User" +i)
                        .lastName("User"+ i)
                        .password("Gizliparola_123")
                        .username("user"+i)
                        .nvsUser(NvsUser.builder().build())
                        .userAddress(userAddress)
                        .serviceAddress(serviceAddress)
                        .roles(Sets.newHashSet(Role.USER))
                        .build();

                nvsLoginDto = NvsLoginDto.builder()
                        .clientId(nvsProjectProperties.getClientId())
                        .grantType("password")
                        .password(user.getPassword())
                        .scope("openid")
                        .email(user.getEmail())
                        .build();
                NvsTokenInfo nvsTokenInfo = nvsUserService.authUserForNvs(nvsLoginDto);
                NvsUser nvsUser = NvsUserUtils.getNvsUserFromIdToken(nvsTokenInfo.getIdToken());

                user.setNvsUser(nvsUser);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }

        };
    }


}


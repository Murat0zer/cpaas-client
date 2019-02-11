package com.netas.cpaas;

import com.hazelcast.core.HazelcastInstance;
import com.netas.cpaas.user.model.NvsLoginDto;
import com.netas.cpaas.user.RoleRepository;
import com.netas.cpaas.user.UserRepository;
import com.netas.cpaas.user.model.NvsTokenInfo;
import com.netas.cpaas.user.model.Role;
import com.netas.cpaas.user.model.User;
import com.netas.cpaas.user.service.NvsUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.netas.cpaas"})
public class CpaasApplication {

	private final PasswordEncoder passwordEncoder;

	private final HazelcastInstance hazelcastInstance;

	private final NvsProjectProperties nvsProjectProperties;

	public static void main(String[] args) {
		SpringApplication.run(CpaasApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository,
										RoleRepository roleRepository,
										NvsUserService nvsUserService) {
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

		};
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}


package com.netas.cpaas;

import com.netas.cpaas.user.model.Role;
import com.netas.cpaas.user.RoleRepository;
import com.netas.cpaas.user.model.User;
import com.netas.cpaas.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.netas.cpaas"})
public class CpaasApplication {

	private final PasswordEncoder passwordEncoder;

	public CpaasApplication(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(CpaasApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository) {
		return args -> {

			if (roleRepository.findAll().isEmpty()) {
				roleRepository.save(Role.ADMIN);
				roleRepository.save(Role.USER);
			}
			User user = User.builder().username("user1").password(passwordEncoder.encode("User1?123")).build();
			Set<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByRoleName(Role.RoleName.USER));
			user.setRoles(roles);

			userRepository.save(user);
		};
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}


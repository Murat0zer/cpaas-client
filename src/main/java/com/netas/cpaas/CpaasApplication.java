package com.netas.cpaas;

import com.netas.cpaas.user.Role;
import com.netas.cpaas.user.RoleRepository;
import com.netas.cpaas.user.User;
import com.netas.cpaas.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

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
			User user = User.builder().username("test").password(passwordEncoder.encode("123456")).build();
			Set<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByRoleName(Role.RoleName.USER));
			user.setRoles(roles);

			userRepository.save(user);
		};
	}
}


package com.dl1803.identity.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dl1803.identity.entity.Role;
import com.dl1803.identity.entity.User;
import com.dl1803.identity.repository.RoleRepository;
import com.dl1803.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Init application!");
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(com.dl1803.identity.enums.Role.USER.toString())
                        .description("User role")
                        .build());
                Role adminRole = roleRepository.save(Role.builder()
                        .name(com.dl1803.identity.enums.Role.ADMIN.toString())
                        .description("Admin role")
                        .build());
                var roles = new HashSet<Role>();
                roles.add(adminRole);
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode(("admin")))
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("Admin user has been created with password: admin, please change it!");
            }
        };
    }
}

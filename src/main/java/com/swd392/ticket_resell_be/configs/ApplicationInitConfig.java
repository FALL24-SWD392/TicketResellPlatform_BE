package com.swd392.ticket_resell_be.configs;

import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("12345678"));
                user.setEmail("datnhse170330@fpt.edu.vn");
                user.setStatus(Categorize.ACTIVE);
                user.setRole(Categorize.ADMIN);
                user.setTypeRegister(Categorize.SYSTEM);

                userRepository.save(user);
            }
        };
    }
}

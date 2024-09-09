package com.swd392.ticket_resell_be.configs;

import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationInitConfig {
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                User user = User.builder()
                        .username("admin")
                        .password("12345678")
                        .build();

                userRepository.save(user);
            }
        };
    }
}

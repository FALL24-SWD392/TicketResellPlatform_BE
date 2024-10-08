package com.swd392.ticket_resell_be.configs;

import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Date;

@Configuration
@EnableAsync
public class ApplicationInitConfig {

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                User user = new User();
                user.setUsername("admin");
                user.setPassword("$2a$10$xkNc05o0d5SP5ZdZ24t7qO4Y9b7BvJOTRYdAvRmx5nnrkTaVQUeXq");
                user.setEmail("datnhse170330@fpt.edu.vn");
                user.setRole(Categorize.ADMIN);
                user.setStatus(Categorize.VERIFIED);
                user.setTypeRegister(Categorize.GOOGLE);
                user.setAvatar("https://i.imgur.com/4Z2ZQ9s.png");
                user.setCreatedBy("admin");
                user.setCreatedAt(new Date());
                user.setUpdatedBy("admin");
                user.setUpdatedAt(new Date());
                userRepository.save(user);
            }
        };
    }
}

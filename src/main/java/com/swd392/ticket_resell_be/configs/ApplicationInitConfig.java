package com.swd392.ticket_resell_be.configs;

import com.swd392.ticket_resell_be.entities.Member;
import com.swd392.ticket_resell_be.repositories.MemberRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationInitConfig {
    @Bean
    ApplicationRunner applicationRunner(MemberRepository memberRepository) {
        return args -> {
            if (memberRepository.findByUsername("admin").isEmpty()) {

                Member member = Member.builder()
                        .username("admin")
                        .password("12345678")
                        .build();

                memberRepository.save(member);
            }
        };
    }
}

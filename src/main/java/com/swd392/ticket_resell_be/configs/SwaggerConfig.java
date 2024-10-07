package com.swd392.ticket_resell_be.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import java.util.Arrays;

import io.swagger.v3.oas.models.tags.Tag;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig(){
        return new OpenAPI()
                .info(
                new Info().title("Ticket Resell App APIs")
                        .description("By SWD392")
                ).servers(Arrays.asList(
                            new Server().url("http://localhost:8081").description("local"),
                            new Server().url("https://api.ticketresell.thucnee.studio/").description("production")
                        )
                ).tags(Arrays.asList(
                        new Tag().name("Authentication APIs"),
                        new Tag().name("Chat APIs"),
                        new Tag().name("Feedback APIs"),
                        new Tag().name("Payment APIs"),
                        new Tag().name("Report APIs"),
                        new Tag().name("Subscription APIs"),
                        new Tag().name("Ticket APIs"),
                        new Tag().name("Transaction APIs"),
                        new Tag().name("User APIs")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ))
                ;
    }
}

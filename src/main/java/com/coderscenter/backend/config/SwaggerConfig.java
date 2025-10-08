package com.coderscenter.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coders Center")
                        .description("Dokumentation der Coders Center App REST API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Emre Imamoglu & Ettore Junior Pesendorfer-Wagner")
                                .email("mreimamoglu@gmail.com & ettore.pw@gmail.com")
                                .url("https://www.coderscenter.com"))
                )
                .servers(List.of(new Server().url("http://localhost:8080").description("Lokale Umgebung")))
                .components(new Components()
                        .addSecuritySchemes("bearer-key", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("openai-public")
                .pathsToMatch("/api/**")
                .build();
    }
}

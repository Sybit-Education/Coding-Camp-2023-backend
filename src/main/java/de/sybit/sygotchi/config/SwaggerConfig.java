package de.sybit.sygotchi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI basicOpenAPI() {
        return new OpenAPI()
                .info(getInfo())
                .servers(getServerList())
                .schemaRequirement("bearerAuth", getSecurityScheme());
    }

    private Info getInfo() {
        return new Info().title("SyGotchi API").description("This is a REST API for SyGotchi.");
    }

    private SecurityScheme getSecurityScheme() {
        return new io.swagger.v3.oas.models.security.SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    private List<Server> getServerList() {
        return List.of(
                new io.swagger.v3.oas.models.servers.Server().url("http://localhost:8080").description("Local Server")
        );
    }
}


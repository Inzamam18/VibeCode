package com.smartshop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI smartShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SmartShop AI REST API")
                        .description("Backend APIs for SmartShop AI - AI-Powered Personal Shopping Assistant. "
                                + "Provides product discovery, filtering, deterministic recommendation engine, "
                                + "Gemini AI requirement extraction, wishlists, and product reviews.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SmartShop Engineering")
                                .email("support@smartshop.ai"))
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Local Development Server")
                ));
    }
}

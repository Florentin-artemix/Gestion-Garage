package com.garage.gestionGarage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // autoriser toutes les routes de ton API
                        .allowedOrigins("*") // ou remplacer par "http://localhost:5500" si tu veux limiter
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // tous les types de requêtes
                        .allowedHeaders("*"); // autoriser tous les headers
            }
        };
    }
}

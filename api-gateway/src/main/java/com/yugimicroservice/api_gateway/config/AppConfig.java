package com.yugimicroservice.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }

    /*@Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of("http://localhost:4200"));
        corsConfig.setAllowedOrigins(List.of("http://localhost:4200"));
        corsConfig.setAllowedMethods(List.of("PUT", "GET", "POST", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Content-Type: Application/json", "Authorization"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        return new CorsWebFilter(exchange -> corsConfig);
    }*/
}

package com.yugimicroservice.strategy_microservice.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplateStrategy(RestTemplateBuilder templateBuilder) {
        return  templateBuilder.build();
    }
}

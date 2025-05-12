package com.fintech.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced // обязательно!
    @Profile("!test & !testcontainers")
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}

package com.fintech.gateway.auth.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
@Profile("testcontainers")
public class TestContainersWebClientConfig {

    @Bean
    WebClient.Builder webClientBuilder(@Value("${currency.service.base-url}") String baseUrl) {
        return WebClient.builder()
                .filter((request, next) -> {
                    URI original = request.url();
                    if (original.toString().contains("http://currency-service")) {
                        URI redirected = URI.create(original.toString().replace("http://currency-service", baseUrl));
                        ClientRequest newRequest = ClientRequest.from(request).url(redirected).build();
                        return next.exchange(newRequest);
                    }
                    return next.exchange(request);
                });
    }
}

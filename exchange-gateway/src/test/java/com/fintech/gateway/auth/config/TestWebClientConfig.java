package com.fintech.gateway.auth.config;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@TestConfiguration
public class TestWebClientConfig {

    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(redirectCurrencyServiceToWireMock());
    }

    private ExchangeFilterFunction redirectCurrencyServiceToWireMock() {
        return (request, next) -> {
            URI original = request.url();
            if (original.toString().contains("http://currency-service")) {
                URI redirected = URI.create(original.toString().replace("http://currency-service", "http://localhost:8089"));
                ClientRequest newRequest = ClientRequest.from(request).url(redirected).build();
                return next.exchange(newRequest);
            }
            return next.exchange(request);
        };
    }
}

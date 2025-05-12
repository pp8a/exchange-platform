package com.fintech.gateway.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    private static final String HEADER_PREFIX = "Bearer ";
    private static final String USER_HEADER = "X-USER-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(HEADER_PREFIX)) {
            return chain.filter(exchange); // без авторизации — пропускаем как есть
        }

        String token = authHeader.substring(HEADER_PREFIX.length());

        UUID userId;
        try {
            userId = jwtUtil.validateTokenAndGetUserId(token);
        } catch (Exception e) {
            log.warn("❌ Invalid JWT: {}", e.getMessage());
            return chain.filter(exchange); // или return unauthorized response
        }

        // добавляем X-USER-ID в новый запрос
        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header(USER_HEADER, userId.toString())
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        return chain.filter(mutatedExchange);
    }
}

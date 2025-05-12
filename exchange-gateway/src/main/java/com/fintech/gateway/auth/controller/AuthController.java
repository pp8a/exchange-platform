package com.fintech.gateway.auth.controller;

import com.fintech.gateway.auth.dto.AuthRequest;
import com.fintech.gateway.auth.dto.AuthResponse;
import com.fintech.gateway.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AuthResponse> getToken(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }
}

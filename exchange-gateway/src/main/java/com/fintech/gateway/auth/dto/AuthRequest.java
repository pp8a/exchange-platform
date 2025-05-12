package com.fintech.gateway.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank String identifier, // это либо email, либо phone
        @NotBlank String password
) {}

package com.fintech.currency.dto;

import java.time.Instant;

public record ErrorResponseDto(
        Instant timestamp,
        int code,
        String message
) {}

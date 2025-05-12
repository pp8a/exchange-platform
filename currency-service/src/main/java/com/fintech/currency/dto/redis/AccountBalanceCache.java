package com.fintech.currency.dto.redis;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountBalanceCache(
        UUID userId,
        BigDecimal balance,
        LocalDateTime cachedAt
) {}
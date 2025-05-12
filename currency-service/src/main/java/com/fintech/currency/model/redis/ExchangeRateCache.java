package com.fintech.currency.model.redis;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateCache {
	private String baseCurrency;     // Базовая валюта (например, USD)
    private String targetCurrency;   // Целевая валюта (например, EUR)
    private BigDecimal rate;         // Курс
    private LocalDateTime timestamp; // Время получения/обновления
}

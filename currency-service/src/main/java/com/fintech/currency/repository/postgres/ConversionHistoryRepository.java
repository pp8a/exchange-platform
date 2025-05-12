package com.fintech.currency.repository.postgres;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.fintech.currency.model.postgres.ConversionHistory;

import reactor.core.publisher.Flux;

public interface ConversionHistoryRepository extends R2dbcRepository<ConversionHistory, UUID> {
	// Получить историю конверсий по валютной паре
    Flux<ConversionHistory> findByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency);

}

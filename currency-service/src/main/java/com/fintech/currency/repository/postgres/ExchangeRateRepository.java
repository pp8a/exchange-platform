package com.fintech.currency.repository.postgres;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.fintech.currency.model.postgres.ExchangeRate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;



public interface ExchangeRateRepository extends R2dbcRepository<ExchangeRate, UUID> {
	// Найти последний актуальный курс валюты для пары
	Mono<ExchangeRate> findTopByBaseCurrencyAndTargetCurrencyOrderByTimestampDesc(
			String baseCurrency, 
			String targetCurrency);
	// Найти все курсы для валютной пары за указанную дату
	Flux<ExchangeRate> findByTimestampBetweenAndBaseCurrencyAndTargetCurrency(
			LocalDate startDate, 
			LocalDate endDate, 
			String baseCurrency, 
			String targetCurrency);
}

package com.fintech.currency.repository.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fintech.currency.model.mongo.CurrencyEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyEventRepository extends ReactiveMongoRepository<CurrencyEvent, String> {
	// Найти события по коду валюты
	Flux<CurrencyEvent> findByCurrencyCodeOrderByTimestampDesc(String currencyCode);
	// Найти событие по ID (опционально)
    Mono<CurrencyEvent> findByEventId(String eventId);
}

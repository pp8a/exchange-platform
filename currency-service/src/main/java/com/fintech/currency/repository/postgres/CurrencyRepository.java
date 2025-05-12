package com.fintech.currency.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fintech.currency.model.postgres.Currency;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyRepository extends ReactiveCrudRepository<Currency, UUID>{
	// Найти валюту по ISO-коду
    Mono<Currency> findByCode(String code);    
    // Получить все валюты
    Flux<Currency> findAll();    
}

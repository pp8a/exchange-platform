package com.fintech.currency.repository.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fintech.currency.model.mongo.ConversionEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConversionEventRepository extends ReactiveMongoRepository<ConversionEvent, String> {
	// Найти события по клиенту
    Flux<ConversionEvent> findByClientIdOrderByTimestampDesc(String clientId);

    // Найти по transactionId
    Mono<ConversionEvent> findByTransactionId(String transactionId);

}

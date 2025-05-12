package com.fintech.currency.repository.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fintech.currency.model.mongo.FeeEvent;

import reactor.core.publisher.Mono;

public interface FeeEventRepository extends ReactiveMongoRepository<FeeEvent, String> {
	// Найти комиссию по transactionId
    Mono<FeeEvent> findByTransactionId(String transactionId);
}

package com.fintech.currency.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fintech.currency.model.postgres.EmailData;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmailDataRepository extends ReactiveCrudRepository<EmailData, UUID> {
	Flux<EmailData> findByUserId(UUID userId);
    Mono<Boolean> existsByEmail(String email);
    Mono<Void> deleteByEmail(String email);
    Mono<EmailData> findByEmail(String email);

}

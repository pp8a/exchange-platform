package com.fintech.currency.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fintech.currency.model.postgres.PhoneData;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhoneDataRepository extends ReactiveCrudRepository<PhoneData, UUID> {
	Flux<PhoneData> findByUserId(UUID userId);
    Mono<Boolean> existsByPhoneNumber(String phoneNumber);
    Mono<Void> deleteByPhoneNumber(String phoneNumber);
    Mono<PhoneData> findByPhoneNumber(String phoneNumber);
}

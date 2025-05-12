package com.fintech.currency.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fintech.currency.model.postgres.Account;

import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<Account, UUID> {
	Mono<Account> findByUserId(UUID userId);
}

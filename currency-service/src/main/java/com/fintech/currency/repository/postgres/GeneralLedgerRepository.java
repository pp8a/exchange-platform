package com.fintech.currency.repository.postgres;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.fintech.currency.model.postgres.GeneralLedger;

import reactor.core.publisher.Mono;

public interface GeneralLedgerRepository extends R2dbcRepository<GeneralLedger, UUID> {
	// Найти запись по идентификатору транзакции
    Mono<GeneralLedger> findByTransactionId(UUID transactionId);
}

package com.fintech.currency.service.api;

import java.util.UUID;

import com.fintech.currency.dto.postgres.GeneralLedgerDTO;

import reactor.core.publisher.Mono;

public interface GeneralLedgerService {
	Mono<GeneralLedgerDTO> getByTransactionId(UUID transactionId);
    Mono<GeneralLedgerDTO> create(GeneralLedgerDTO dto);
    Mono<GeneralLedgerDTO> update(UUID id, GeneralLedgerDTO dto);
    Mono<Void> delete(UUID id);
}

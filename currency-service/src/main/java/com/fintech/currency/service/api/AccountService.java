package com.fintech.currency.service.api;

import java.math.BigDecimal;
import java.util.UUID;

import com.fintech.currency.dto.postgres.AccountDTO;
import com.fintech.currency.model.mongo.TransferLog;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
	Mono<Integer> increaseBalances();
	Mono<AccountDTO> getByUserId(UUID userId);
	Mono<Void> transfer(UUID fromUserId, UUID toUserId, BigDecimal amount);
	Mono<AccountDTO> getFromDbByUserId(UUID userId);	
	Flux<TransferLog> getTransferHistory(String userId);
}

package com.fintech.currency.repository.postgres;

import java.math.BigDecimal;
import java.util.UUID;

import reactor.core.publisher.Mono;

public interface AccountCustomRepository {
	Mono<Integer> updateBalanceIfLessThanMax();
	Mono<Void> transferBetweenUsers(UUID fromUserId, UUID toUserId, BigDecimal amount);
}

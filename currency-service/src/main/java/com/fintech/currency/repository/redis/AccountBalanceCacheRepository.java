package com.fintech.currency.repository.redis;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import com.fintech.currency.dto.redis.AccountBalanceCache;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccountBalanceCacheRepository {
	private final ReactiveRedisOperations<String, AccountBalanceCache> redisOps;
	
	public Mono<Void> save(UUID userId, AccountBalanceCache dto, Duration ttl) {
        return redisOps.opsForValue()
            .set("balance:" + userId, dto, ttl)
            .then();
    }
	
	public Mono<AccountBalanceCache> find(UUID userId) {
        return redisOps.opsForValue().get("balance:" + userId);
    }
	
	public Mono<Boolean> delete(UUID userId) {
        return redisOps.delete("balance:" + userId)
                .map(count -> count > 0);
    }

}

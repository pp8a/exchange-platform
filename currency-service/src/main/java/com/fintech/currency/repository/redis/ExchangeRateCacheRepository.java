package com.fintech.currency.repository.redis;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import com.fintech.currency.model.redis.ExchangeRateCache;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ExchangeRateCacheRepository {
	
	private final ReactiveRedisOperations<String, ExchangeRateCache> redisOps;
	
	// ✅ Сохранить курс по ключу
    public Mono<Void> saveRate(String key, ExchangeRateCache rate) {
        return redisOps.opsForValue().set(key, rate).then();
    }

    // ✅ Получить курс по ключу
    public Mono<ExchangeRateCache> findRate(String key) {
        return redisOps.opsForValue().get(key);
    }

    // ✅ Удалить курс по ключу
    public Mono<Boolean> deleteRate(String key) {    	
        return redisOps.delete(key) //1, если ключ был и удалён 0, если ключа не было
        		.map(deletedCount -> deletedCount > 0); // true, если что-то удалили
    }
}

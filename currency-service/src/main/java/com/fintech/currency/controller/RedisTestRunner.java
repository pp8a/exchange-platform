package com.fintech.currency.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fintech.currency.model.redis.ExchangeRateCache;
import com.fintech.currency.repository.redis.ExchangeRateCacheRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisTestRunner implements CommandLineRunner {
	private final ExchangeRateCacheRepository cacheRepository;

	@Override
	public void run(String... args) throws Exception {
		String key = "USD-EUR";
        ExchangeRateCache rate = new ExchangeRateCache(
                "USD", "EUR", new BigDecimal("0.95"), LocalDateTime.now()
        );

        cacheRepository.saveRate(key, rate)
                .doOnSuccess(v -> System.out.println("✅ Сохранено в Redis: " + key))
                .then(cacheRepository.findRate(key))
                .doOnNext(found -> System.out.println("📦 Получено из Redis: " + found))
                .subscribe();

	}

}


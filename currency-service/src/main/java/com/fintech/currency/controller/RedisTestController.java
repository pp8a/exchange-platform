package com.fintech.currency.controller;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisTestController {
	private final ReactiveRedisTemplate<String, String> redisTemplate;
	
	@GetMapping("/ping")
    public Mono<String> pingRedis() {
        return redisTemplate.execute(connection -> connection.ping()).next();
    }
}

package com.fintech.currency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fintech.currency.dto.redis.AccountBalanceCache;
import com.fintech.currency.model.redis.ExchangeRateCache;


@Configuration
public class RedisConfig {
	@Bean
    public ReactiveRedisOperations<String, AccountBalanceCache> accountBalanceRedisOperations(ReactiveRedisConnectionFactory factory) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<AccountBalanceCache> serializer = new Jackson2JsonRedisSerializer<>(AccountBalanceCache.class);
        serializer.setObjectMapper(objectMapper);

        RedisSerializationContext<String, AccountBalanceCache> context = RedisSerializationContext
                .<String, AccountBalanceCache>newSerializationContext(new StringRedisSerializer())
                .value(serializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
	
	@Bean
	ReactiveRedisOperations<String, ExchangeRateCache> exchangeRateRedisOperations(ReactiveRedisConnectionFactory factory) {
	    ObjectMapper objectMapper = new ObjectMapper()
	            .registerModule(new JavaTimeModule())
	            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	    Jackson2JsonRedisSerializer<ExchangeRateCache> serializer = new Jackson2JsonRedisSerializer<>(ExchangeRateCache.class);
	    serializer.setObjectMapper(objectMapper);

	    RedisSerializationContext<String, ExchangeRateCache> context = RedisSerializationContext
	            .<String, ExchangeRateCache>newSerializationContext(new StringRedisSerializer())
	            .value(serializer)
	            .build();

	    return new ReactiveRedisTemplate<>(factory, context);
	}
	
//    @Bean
//    ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
//
//        // ✅ ObjectMapper с поддержкой времени
//        ObjectMapper objectMapper = new ObjectMapper()
//                .registerModule(new JavaTimeModule()) // Поддержка Java 8+ времени (например, LocalDateTime)
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Форматируем дату в ISO-8601, а не timestamp
//
//        // ✅ Универсальный сериализатор (Generic, без типизации)
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper); //конвертирует любой Java-объект в JSON (и обратно)
//
//        // ✅ Контекст сериализации с явной типизацией ключей и значений
//        RedisSerializationContext<String, Object> context = RedisSerializationContext
//                .<String, Object>newSerializationContext(new StringRedisSerializer()) // сериализация ключей как строк
//                .value(serializer) // сериализация значений как JSON через Jackson
//                .build();
//
//        return new ReactiveRedisTemplate<>(factory, context);
//    }
	
//  // Оборачиваем сериализатор в SerializationPair
//  RedisSerializationContext.SerializationPair<ExchangeRateCache> valueSerializationPair =
//          RedisSerializationContext.SerializationPair.fromSerializer(serializer);
}



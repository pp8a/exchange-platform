package com.fintech.currency.service.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fintech.currency.avro.TransferEvent;
import com.fintech.currency.dto.postgres.AccountDTO;
import com.fintech.currency.dto.redis.AccountBalanceCache;
import com.fintech.currency.exception.EntityNotFoundException;
import com.fintech.currency.exception.UserNotFoundException;
import com.fintech.currency.kafka.producer.UserKafkaProducer;
import com.fintech.currency.mapper.postgres.AccountMapper;
import com.fintech.currency.model.mongo.TransferLog;
import com.fintech.currency.repository.mongo.TransferLogRepository;
import com.fintech.currency.repository.postgres.AccountCustomRepository;
import com.fintech.currency.repository.postgres.AccountRepository;
import com.fintech.currency.repository.redis.AccountBalanceCacheRepository;
import com.fintech.currency.service.api.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final AccountCustomRepository customRepository;
	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;
		
	private final AccountBalanceCacheRepository cacheRepository;
	private final UserKafkaProducer kafkaProducer;
	
	private final TransferLogRepository transferLogRepository;
	
	@Override
	public Mono<Integer> increaseBalances() {
		log.info("📈 Increasing account balances...");
		 return customRepository.updateBalanceIfLessThanMax()
		            .doOnNext(updated -> {
		                if (updated > 0) {
		                    log.info("✅ Updated {} accounts", updated);
		                } else {
		                    log.debug("⏳ No balances updated this cycle");
		                }
		            });
	}
	
	@Override
	public Mono<AccountDTO> getFromDbByUserId(UUID userId) {
		log.info("📌 Fetching user balance directly from DB (bypassing cache): {}", userId);
		return accountRepository.findByUserId(userId)
	            .switchIfEmpty(Mono.error(new EntityNotFoundException("Account not found for user: " + userId)))
	            .map(accountMapper::toDto);
	}
		
	@Override
	public Mono<AccountDTO> getByUserId(UUID userId) {
	    return cacheRepository.find(userId)
	        .map(cache -> {
	            log.info("📦 Cached balance returned for user {}", userId); //во втором запросе
	            AccountDTO dto = new AccountDTO();
	            dto.setUserId(userId);
	            dto.setBalance(cache.balance());
	            dto.setUpdatedAt(cache.cachedAt()); // логично — последнее обновление
	            return dto;
	        })
	        .switchIfEmpty(
	            accountRepository.findByUserId(userId)
	                .flatMap(account -> {
	                    BigDecimal maxBalance = account.getInitialBalance().multiply(BigDecimal.valueOf(2.07));
	                    Duration ttl = account.getBalance().compareTo(maxBalance) >= 0
	                        ? Duration.ofHours(6)
	                        : Duration.ofSeconds(30);

	                    AccountBalanceCache dto = new AccountBalanceCache(userId, account.getBalance(), LocalDateTime.now());
	                    return cacheRepository.save(userId, dto, ttl)
	                            .thenReturn(accountMapper.toDto(account));
	                })
	        );
	}

	@Override
	public Mono<Void> transfer(UUID fromUserId, UUID toUserId, BigDecimal amount) {
		log.info("🔁 Transferring {} from {} to {}", amount, fromUserId, toUserId);
		
		if (fromUserId.equals(toUserId)) {
			return Mono.error(new IllegalArgumentException("Cannot transfer to the same user"));
		}
		
		return accountRepository.findByUserId(toUserId)
				.switchIfEmpty(Mono.error(new UserNotFoundException(toUserId)))
				.flatMap(__ -> customRepository.transferBetweenUsers(fromUserId, toUserId, amount))	
				//.then(kafkaProducer.sendEvent(fromUserId.toString(), accountMapper.toAvro(fromUserId, toUserId, amount)))
				.doOnSuccess(__ -> {
					log.info("✅ Transfer complete");
					TransferEvent event = accountMapper.toAvro(fromUserId, toUserId, amount);
					kafkaProducer.sendEvent(fromUserId.toString(), event)
						.doOnError(e -> log.error("❌ Kafka send failed: {}", e.getMessage()))
						.subscribe();
				})
				.doOnError(e -> log.error("❌ Transfer failed: {}", e.getMessage()));
	}

	@Override
	public Flux<TransferLog> getTransferHistory(String userId) {
		log.info("📜 Fetching transfer history for userId: {}", userId);
	    return transferLogRepository.findAllByFromUserIdOrToUserIdOrderByTimestampDesc(userId, userId);
	}

}

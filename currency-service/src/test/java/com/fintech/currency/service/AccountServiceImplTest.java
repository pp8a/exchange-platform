package com.fintech.currency.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import com.fintech.currency.exception.UserNotFoundException;
import com.fintech.currency.kafka.producer.UserKafkaProducer;
import com.fintech.currency.mapper.postgres.AccountMapper;
import com.fintech.currency.model.postgres.Account;
import com.fintech.currency.repository.mongo.TransferLogRepository;
import com.fintech.currency.repository.postgres.AccountCustomRepository;
import com.fintech.currency.repository.postgres.AccountRepository;
import com.fintech.currency.repository.redis.AccountBalanceCacheRepository;
import com.fintech.currency.service.impl.AccountServiceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

class AccountServiceImplTest {
	private AccountCustomRepository customRepository;
    private AccountRepository accountRepository;
    private AccountBalanceCacheRepository cacheRepository;
    private AccountMapper accountMapper;
    private UserKafkaProducer kafkaProducer; 
    private AccountServiceImpl service;
    private TransferLogRepository transferLogRepository;
    
    private final UUID fromUserId = UUID.randomUUID();
    private final UUID toUserId = UUID.randomUUID();
    private final BigDecimal amount = new BigDecimal("100.00");
    
    @BeforeEach
    void setUp() {
        customRepository = mock(AccountCustomRepository.class);
        accountRepository = mock(AccountRepository.class);
        cacheRepository = mock(AccountBalanceCacheRepository.class);
        accountMapper = mock(AccountMapper.class);
        kafkaProducer = mock(UserKafkaProducer.class);
        transferLogRepository = mock(TransferLogRepository.class);
        
        when(kafkaProducer.sendEvent(anyString(), any())).thenReturn(Mono.empty());

        service = new AccountServiceImpl(
        		customRepository, 
        		accountRepository, 
        		accountMapper, 
        		cacheRepository, 
        		kafkaProducer, 
        		transferLogRepository);
    }
    
    @Test
    @DisplayName("✅ Should transfer money when balance is sufficient")    
    void shouldTransferSuccessfully() {
        when(accountRepository.findByUserId(toUserId)).thenReturn(Mono.just(new Account()));
        when(customRepository.transferBetweenUsers(fromUserId, toUserId, amount)).thenReturn(Mono.empty());

        StepVerifier.create(service.transfer(fromUserId, toUserId, amount))
                .verifyComplete();

        verify(accountRepository).findByUserId(toUserId);
        verify(customRepository).transferBetweenUsers(fromUserId, toUserId, amount);
    }
        
    @Test
    @DisplayName("❌ Should throw UserNotFoundException when receiver account is missing")    
    void shouldThrowWhenToUserNotFound() {
        when(accountRepository.findByUserId(toUserId)).thenReturn(Mono.empty());

        StepVerifier.create(service.transfer(fromUserId, toUserId, amount))
                .expectError(UserNotFoundException.class)
                .verify();

        verify(accountRepository).findByUserId(toUserId);
        verify(customRepository, never()).transferBetweenUsers(any(), any(), any());
    }
    
    @Test
    @DisplayName("❌ Should throw IllegalArgumentException on self-transfer attempt")    
    void shouldThrowWhenSameUserIds() {
        StepVerifier.create(service.transfer(fromUserId, fromUserId, amount))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(accountRepository, never()).findByUserId(any());
        verify(customRepository, never()).transferBetweenUsers(any(), any(), any());
    }
}

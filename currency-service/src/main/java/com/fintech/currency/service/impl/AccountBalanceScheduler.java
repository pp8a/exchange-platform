package com.fintech.currency.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fintech.currency.service.api.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBalanceScheduler {
	private final AccountService balanceService;
	
	@Scheduled(fixedRate = 30_000)
    public void runBalanceUpdate() {
        log.info("⏰ Triggering scheduled balance update...");
        balanceService.increaseBalances()
                .doOnError(e -> log.error("❌ Balance update failed: {}", e.getMessage()))
                .subscribe();
    }
}

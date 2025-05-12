package com.fintech.currency.service.api;

import java.time.LocalDate;
import java.util.UUID;

import com.fintech.currency.dto.postgres.ExchangeRateDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Mono<ExchangeRateDTO> getById(UUID id);
    Mono<ExchangeRateDTO> getLatestRate(String base, String target);
    Flux<ExchangeRateDTO> getByDateRange(LocalDate start, LocalDate end, String base, String target);
    Mono<ExchangeRateDTO> create(ExchangeRateDTO dto);
    Mono<ExchangeRateDTO> update(UUID id, ExchangeRateDTO dto);
    Mono<Void> delete(UUID id);
}

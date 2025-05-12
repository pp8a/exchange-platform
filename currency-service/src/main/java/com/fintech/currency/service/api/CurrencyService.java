package com.fintech.currency.service.api;

import java.util.UUID;

import com.fintech.currency.dto.postgres.CurrencyDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {
	Mono<CurrencyDTO> getByCode(String code);
    Flux<CurrencyDTO> getAll();
    Mono<CurrencyDTO> getById(UUID id);
    Mono<CurrencyDTO> create(CurrencyDTO dto);
    Mono<CurrencyDTO> update(UUID id, CurrencyDTO dto);
    Mono<Void> delete(UUID id);
}

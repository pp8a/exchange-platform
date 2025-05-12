package com.fintech.currency.service.api;

import java.util.UUID;

import com.fintech.currency.dto.postgres.ConversionHistoryDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConversionHistoryService {
	Flux<ConversionHistoryDTO> getByCurrencyPair(String base, String target);
    Mono<ConversionHistoryDTO> create(ConversionHistoryDTO dto);
    Mono<ConversionHistoryDTO> update(UUID id, ConversionHistoryDTO dto);
    Mono<Void> delete(UUID id);
}

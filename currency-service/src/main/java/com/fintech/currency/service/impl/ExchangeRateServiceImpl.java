package com.fintech.currency.service.impl;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.fintech.currency.dto.postgres.ExchangeRateDTO;
import com.fintech.currency.mapper.postgres.ExchangeRateMapper;
import com.fintech.currency.repository.postgres.ExchangeRateRepository;
import com.fintech.currency.service.api.ExchangeRateService;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
	private final ExchangeRateRepository exchangeRateRepository;
	private final ExchangeRateMapper exchangeRateMapper;
	
	@Override
	public Mono<ExchangeRateDTO> getById(UUID id) {		
		return exchangeRateRepository
				.findById(id)
				.map(exchangeRateMapper::toDto);
	}

	@Override
	public Mono<ExchangeRateDTO> getLatestRate(String base, String target) {
		return exchangeRateRepository
				.findTopByBaseCurrencyAndTargetCurrencyOrderByTimestampDesc(base, target)
				.map(exchangeRateMapper::toDto);
	}

	@Override
	public Flux<ExchangeRateDTO> getByDateRange(LocalDate start, LocalDate end, String base, String target) {		
		return exchangeRateRepository
				.findByTimestampBetweenAndBaseCurrencyAndTargetCurrency(start, end, base, target)
				.map(exchangeRateMapper::toDto);
	}

	@Override
	public Mono<ExchangeRateDTO> create(ExchangeRateDTO dto) {
		return exchangeRateRepository
				.save(exchangeRateMapper.toEntity(dto))
                .map(exchangeRateMapper::toDto);
	}

	@Override
	public Mono<ExchangeRateDTO> update(UUID id, ExchangeRateDTO dto) {		
		return exchangeRateRepository.findById(id)
				.map(existing -> {
					exchangeRateMapper.updateFromDto(dto, existing);
					return existing;
				})
				.flatMap(exchangeRateRepository::save)
				.map(exchangeRateMapper::toDto);
	}

	@Override
	public Mono<Void> delete(UUID id) {		
		return exchangeRateRepository.deleteById(id);
	}

}

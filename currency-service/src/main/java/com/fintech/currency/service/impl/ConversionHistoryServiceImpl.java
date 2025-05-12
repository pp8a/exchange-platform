package com.fintech.currency.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fintech.currency.dto.postgres.ConversionHistoryDTO;
import com.fintech.currency.mapper.postgres.ConversionHistoryMapper;
import com.fintech.currency.repository.postgres.ConversionHistoryRepository;
import com.fintech.currency.service.api.ConversionHistoryService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConversionHistoryServiceImpl implements ConversionHistoryService {
	private final ConversionHistoryRepository conversionHistoryRepository;
	private final ConversionHistoryMapper conversionHistoryMapper;

	@Override
	public Flux<ConversionHistoryDTO> getByCurrencyPair(String base, String target) {		
		return conversionHistoryRepository
				.findByBaseCurrencyAndTargetCurrency(base, target)
				.map(conversionHistoryMapper::toDto);
	}

	@Override
	public Mono<ConversionHistoryDTO> create(ConversionHistoryDTO dto) {		
		return conversionHistoryRepository
				.save(conversionHistoryMapper.toEntity(dto))
				.map(conversionHistoryMapper::toDto);
	}

	@Override
	public Mono<ConversionHistoryDTO> update(UUID id, ConversionHistoryDTO dto) {		
		return conversionHistoryRepository.findById(id).map(existing -> {
			conversionHistoryMapper.updateFromDto(dto, existing);
			return existing;
		}).flatMap(conversionHistoryRepository::save).map(conversionHistoryMapper::toDto);
	}

	@Override
	public Mono<Void> delete(UUID id) {
		return conversionHistoryRepository.deleteById(id);
	}

}
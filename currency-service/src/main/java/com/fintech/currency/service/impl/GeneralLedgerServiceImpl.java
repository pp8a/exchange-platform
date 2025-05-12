package com.fintech.currency.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fintech.currency.dto.postgres.GeneralLedgerDTO;
import com.fintech.currency.mapper.postgres.GeneralLedgerMapper;
import com.fintech.currency.repository.postgres.GeneralLedgerRepository;
import com.fintech.currency.service.api.GeneralLedgerService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GeneralLedgerServiceImpl implements GeneralLedgerService {
	private final GeneralLedgerRepository generalLedgerRepository;
	private final GeneralLedgerMapper generalLedgerMapper;

	@Override
	public Mono<GeneralLedgerDTO> getByTransactionId(UUID transactionId) {
		return generalLedgerRepository
				.findByTransactionId(transactionId)
				.map(generalLedgerMapper::toDto);
	}

	@Override
	public Mono<GeneralLedgerDTO> create(GeneralLedgerDTO dto) {		
		return generalLedgerRepository
				.save(generalLedgerMapper.toEntity(dto))
				.map(generalLedgerMapper::toDto);
	}

	@Override
	public Mono<GeneralLedgerDTO> update(UUID id, GeneralLedgerDTO dto) {		
		return generalLedgerRepository.findById(id)
				.map(existing -> {
					generalLedgerMapper.updateFromDto(dto, existing);
					return existing;
				})
				.flatMap(generalLedgerRepository::save)
				.map(generalLedgerMapper::toDto);
	}

	@Override
	public Mono<Void> delete(UUID id) {
		return generalLedgerRepository.deleteById(id);
	}

}

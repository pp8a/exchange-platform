package com.fintech.currency.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fintech.currency.dto.postgres.CurrencyDTO;
import com.fintech.currency.mapper.postgres.CurrencyMapper;
import com.fintech.currency.repository.postgres.CurrencyRepository;
import com.fintech.currency.service.api.CurrencyService;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {
	private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    
	@Override
	public Mono<CurrencyDTO> getByCode(String code) {	
		log.info("➡️ getByCode({}) called with", code);
		return currencyRepository.findByCode(code)
				.map(currencyMapper::toDto);
	}

	@Override
	public Flux<CurrencyDTO> getAll() {
		log.info("➡️ getAll() called");
		return currencyRepository.findAll()
				.map(currencyMapper::toDto);
	}
	
	@Override
    public Mono<CurrencyDTO> getById(UUID id) {
        log.info("➡️ findById({}) called", id);
        return currencyRepository.findById(id)
                .map(currencyMapper::toDto);
    }

	@Override
	public Mono<CurrencyDTO> create(CurrencyDTO dto) {
		log.info("➡️ create() сurrency called");
		return currencyRepository.save(currencyMapper.toEntity(dto))
				.map(currencyMapper::toDto);
	}

	@Override
	public Mono<CurrencyDTO> update(UUID id, CurrencyDTO dto) {	
		log.info("➡️ update({}, dto) called", id);
		return currencyRepository.findById(id)
				.switchIfEmpty(Mono.error(new NotFoundException("Currency not found")))
				.map(existing -> {
					currencyMapper.updateFromDto(dto, existing);
					return existing;
				})
				.flatMap(currencyRepository::save)
				.map(currencyMapper::toDto);
	}

	@Override
	public Mono<Void> delete(UUID id) {
		log.info("➡️ delete({}) called", id);
		return currencyRepository.deleteById(id);
	}

}

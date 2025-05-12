package com.fintech.currency.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fintech.currency.dto.postgres.PhoneDataDTO;
import com.fintech.currency.exception.AccessDeniedException;
import com.fintech.currency.exception.EntityNotFoundException;
import com.fintech.currency.exception.PhoneAlreadyUsedException;
import com.fintech.currency.mapper.postgres.PhoneDataMapper;
import com.fintech.currency.model.postgres.PhoneData;
import com.fintech.currency.repository.postgres.PhoneDataRepository;
import com.fintech.currency.service.api.PhoneDataService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PhoneDataServiceImpl implements PhoneDataService {
	
	private final PhoneDataRepository phoneDataRepository;
	private final PhoneDataMapper phoneMapper; 

	@Override
	public Mono<PhoneDataDTO> addPhone(UUID userId, PhoneDataDTO dto) {
		return phoneDataRepository.existsByPhoneNumber(dto.getPhoneNumber())
                .flatMap(exists -> {
                    if (exists) {
                    	return Mono.error(new PhoneAlreadyUsedException(dto.getPhoneNumber()));
                    }
                    PhoneData entity = phoneMapper.toEntity(dto);
                    entity.setUserId(userId);
                    return phoneDataRepository.save(entity);
                })
                .map(phoneMapper::toDto);
	}

	@Override
	public Mono<Void> deletePhone(UUID userId, String phoneNumber) {
		return phoneDataRepository.existsByPhoneNumber(phoneNumber)
                .flatMap(exists -> {
                    if (!exists) {
                    	return Mono.error(new EntityNotFoundException("Phone not found: " + phoneNumber));
                    }
                    return phoneDataRepository.deleteByPhoneNumber(phoneNumber);
                });
	}

	@Override
	public Mono<PhoneDataDTO> updatePhone(UUID userId, UUID phoneId, PhoneDataDTO dto) {
		return phoneDataRepository.existsByPhoneNumber(dto.getPhoneNumber())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new PhoneAlreadyUsedException(dto.getPhoneNumber()));
                    }
                    return phoneDataRepository.findById(phoneId)
                            .switchIfEmpty(Mono.error(new EntityNotFoundException("PhoneData not found: " + phoneId)))
                            .flatMap(existing -> {
                                if (!existing.getUserId().equals(userId)) {
                                    return Mono.error(new AccessDeniedException("Access denied: phone doesn't belong to user"));
                                }
                                phoneMapper.updateFromDto(dto, existing);
                                return phoneDataRepository.save(existing);
                            });
                })
                .map(phoneMapper::toDto);
	}

	@Override
	public Mono<UUID> findUserIdByPhone(String phone) {
		return phoneDataRepository.findByPhoneNumber(phone)
		        .map(PhoneData::getUserId)
		        .switchIfEmpty(Mono.error(new EntityNotFoundException("Phone not found" + phone)));
	}
	
	

}

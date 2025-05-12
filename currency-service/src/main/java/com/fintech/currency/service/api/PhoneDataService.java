package com.fintech.currency.service.api;

import java.util.UUID;

import com.fintech.currency.dto.postgres.PhoneDataDTO;

import reactor.core.publisher.Mono;

public interface PhoneDataService {
	Mono<PhoneDataDTO> addPhone(UUID userId, PhoneDataDTO dto);
    Mono<Void> deletePhone(UUID userId, String phoneNumber);
    Mono<PhoneDataDTO> updatePhone(UUID userId, UUID phoneId, PhoneDataDTO dto);
    Mono<UUID> findUserIdByPhone(String phone);
}

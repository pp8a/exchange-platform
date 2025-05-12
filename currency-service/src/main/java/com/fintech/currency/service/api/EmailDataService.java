package com.fintech.currency.service.api;

import java.util.UUID;

import com.fintech.currency.dto.postgres.EmailDataDTO;

import reactor.core.publisher.Mono;

public interface EmailDataService {
	Mono<EmailDataDTO> addEmail(UUID userId, EmailDataDTO dto);
    Mono<Void> deleteEmail(UUID userId, String email);
    Mono<EmailDataDTO> updateEmail(UUID userId, UUID emailId, EmailDataDTO dto);
    Mono<UUID> findUserIdByEmail(String email);
}

package com.fintech.currency.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fintech.currency.dto.postgres.EmailDataDTO;
import com.fintech.currency.exception.AccessDeniedException;
import com.fintech.currency.exception.EmailAlreadyUsedException;
import com.fintech.currency.exception.EntityNotFoundException;
import com.fintech.currency.mapper.postgres.EmailDataMapper;
import com.fintech.currency.model.postgres.EmailData;
import com.fintech.currency.repository.postgres.EmailDataRepository;
import com.fintech.currency.service.api.EmailDataService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailDataServiceImpl implements EmailDataService {
	
	private final EmailDataRepository emailDataRepository;
	private final EmailDataMapper emailMapper;

	@Override
	public Mono<EmailDataDTO> addEmail(UUID userId, EmailDataDTO dto) {
		return emailDataRepository.existsByEmail(dto.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                    	return Mono.error(new EmailAlreadyUsedException(dto.getEmail()));
                    }
                    EmailData entity = emailMapper.toEntity(dto);
                    entity.setUserId(userId);
                    return emailDataRepository.save(entity);
                })
                .map(emailMapper::toDto);
	}

	@Override
	public Mono<Void> deleteEmail(UUID userId, String email) {
		return emailDataRepository.existsByEmail(email)
                .flatMap(exists -> {
                    if (!exists) {
                    	return Mono.error(new EntityNotFoundException("Email not found: " + email)); 
                    }
                    return emailDataRepository.deleteByEmail(email);
                });
	}
	
	@Override
	public Mono<EmailDataDTO> updateEmail(UUID userId, UUID emailId, EmailDataDTO dto) {
	    return emailDataRepository.existsByEmail(dto.getEmail())
	        .flatMap(exists -> {
	            if (exists) {
	                return Mono.error(new EmailAlreadyUsedException(dto.getEmail()));
	            }
	            return emailDataRepository.findById(emailId)
	                .switchIfEmpty(Mono.error(new EntityNotFoundException("EmailData not found")))
	                .flatMap(existing -> {
	                    if (!existing.getUserId().equals(userId)) {
	                        return Mono.error(new AccessDeniedException("You can update only your own email"));
	                    }
	                    emailMapper.updateFromDto(dto, existing);
	                    return emailDataRepository.save(existing);
	                });
	        })
	        .map(emailMapper::toDto);
	}

	@Override
	public Mono<UUID> findUserIdByEmail(String email) {
		return emailDataRepository.findByEmail(email)
		        .map(EmailData::getUserId)
		        .switchIfEmpty(Mono.error(new EntityNotFoundException("Email not found: " + email)));
	}

}

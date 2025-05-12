package com.fintech.currency.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.currency.dto.postgres.EmailDataDTO;
import com.fintech.currency.service.api.EmailDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
@Tag(name = "Email Management", description = "Operations for managing user email addresses")
public class EmailDataController {
	private final EmailDataService emailDataService;
	
	@Operation(summary = "Add new email", description = "Add a new email address to the current user")
	@PostMapping
    public Mono<EmailDataDTO> addEmail(
    		@Parameter(description = "ID of the authenticated user") @RequestHeader("X-USER-ID") UUID userId,
            @RequestBody EmailDataDTO dto) {
		log.info("📩 [POST] add email: userId={}, email={}", userId, dto);
        return emailDataService.addEmail(userId, dto);
    }
	
	@Operation(summary = "Delete email", description = "Delete an email address for the current user")
	@DeleteMapping
    public Mono<Void> deleteEmail(
    		@Parameter(description = "ID of the authenticated user") @RequestHeader("X-USER-ID") UUID userId,
    		@Parameter(description = "Email address to delete") @RequestParam String email) {
		log.info("❌ [DELETE] email: userId={}, email={}", userId, email);
        return emailDataService.deleteEmail(userId, email);
    }
	
	@Operation(summary = "Update email", description = "Update an existing email address for the current user")
    @PutMapping("/{emailId}")
    public Mono<EmailDataDTO> updateEmail(
    		@Parameter(description = "ID of the authenticated user") @RequestHeader("X-USER-ID") UUID userId,
    		@Parameter(description = "Email ID to update") @PathVariable UUID emailId,
            @RequestBody EmailDataDTO dto) {
    	log.info("📝 [PUT] update email: userId={}, emailId={}, dto={}", userId, emailId, dto);
        return emailDataService.updateEmail(userId, emailId, dto);
    }
    
	@Operation(summary = "Find user by email", description = "Get user ID associated with the given email")
    @GetMapping("/user-id")
    public Mono<UUID> getUserIdByEmail(
    		@Parameter(description = "Email address to resolve") @RequestParam String email) {
    	log.info("🔍 [GET] find userId by email: {}", email);
    	return emailDataService.findUserIdByEmail(email)
                .doOnSuccess(id -> log.info("✅ userId for email {}: {}", email, id))
                .doOnError(err -> log.error("❌ Error resolving email: {}", email, err));
    }
}

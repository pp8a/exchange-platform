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

import com.fintech.currency.dto.postgres.PhoneDataDTO;
import com.fintech.currency.service.api.PhoneDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/phones")
@RequiredArgsConstructor
@Tag(name = "Phone Management", description = "Operations for managing user phone numbers")
public class PhoneDataController {
	private final PhoneDataService phoneDataService;
	
	@Operation(summary = "Add new phone", description = "Add a new phone number to the current user")
	@PostMapping
    public Mono<PhoneDataDTO> addPhone(
    		@Parameter(description = "ID of the authenticated user") @RequestHeader("X-USER-ID") UUID userId,
            @RequestBody PhoneDataDTO dto) {
        return phoneDataService.addPhone(userId, dto);
    }
	
	@Operation(summary = "Delete phone", description = "Delete a phone number for the current user")
    @DeleteMapping
    public Mono<Void> deletePhone(
    		@Parameter(description = "ID of the authenticated user") @RequestHeader("X-USER-ID") UUID userId,
    		@Parameter(description = "Phone number to delete") @RequestParam String phone) {
        return phoneDataService.deletePhone(userId, phone);
    }
	
	@Operation(summary = "Update phone", description = "Update an existing phone number for the current user")
    @PutMapping("/{phoneId}")
    public Mono<PhoneDataDTO> updatePhone(
    		@Parameter(description = "ID of the authenticated user") @RequestHeader("X-USER-ID") UUID userId,
    		@Parameter(description = "Phone ID to update") @PathVariable UUID phoneId,
            @RequestBody PhoneDataDTO dto) {
        return phoneDataService.updatePhone(userId, phoneId, dto);
    }
    
	@Operation(summary = "Find user by phone", description = "Get user ID associated with the given phone number")
    @GetMapping("/user-id")
    public Mono<UUID> getUserIdByPhone(
    		@Parameter(description = "Phone number to resolve") @RequestParam String phone) {
        return phoneDataService.findUserIdByPhone(phone);
    }

}

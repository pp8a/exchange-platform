package com.fintech.currency.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.currency.dto.postgres.UserDTO;
import com.fintech.currency.dto.postgres.UserProjection;
import com.fintech.currency.service.api.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {
	
	private final UserService userService;
	
	@Operation(
			summary = "Create a new user",
			description = "Creates a new user with associated emails, phones and account balance.",
			responses = {
				@ApiResponse(responseCode = "200", description = "User created successfully", content = @Content),
				@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
			}
	)
    @PostMapping
    public Mono<UserDTO> create(
    		@io.swagger.v3.oas.annotations.parameters.RequestBody(
    				description = "User creation payload",
    				required = true,
    				content = @Content(schema = @Schema(implementation = UserDTO.class))
    		)
    		@RequestBody UserDTO dto) {
    	log.info("📥 [POST] create user: {}", dto);
        return userService.create(dto);
    }
	
	@Operation(
			summary = "Update user data",
			description = "Updates user fields by user ID from token header",
			responses = {
				@ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content),
				@ApiResponse(responseCode = "404", description = "User not found", content = @Content)
			}
	)
    @PutMapping
    public Mono<UserDTO> update(
    		@Parameter(description = "User ID (from JWT or header)", required = true)
    		@RequestHeader("X-USER-ID") UUID id, 
    		@io.swagger.v3.oas.annotations.parameters.RequestBody(
    				description = "User data to update",
    				required = true,
    				content = @Content(schema = @Schema(implementation = UserDTO.class))
    		)
    		@RequestBody UserDTO dto) {
    	log.info("🔄 [PUT] update user: id={}, dto={}", id, dto);
        return userService.update(id, dto);
    }
    
	@Operation(
			summary = "Get user by ID",
			description = "Returns full user data by their UUID",
			responses = {
				@ApiResponse(responseCode = "200", description = "User found", content = @Content),
				@ApiResponse(responseCode = "404", description = "User not found", content = @Content)
			}
	)
    @GetMapping("/{id}")
    public Mono<UserDTO> getById(
    		@Parameter(description = "UUID of the user", required = true)
    		@PathVariable UUID id) {
    	log.info("📤 [GET] get user by id: {}", id);
        return userService.getById(id);
    }	
    
	@Operation(
			summary = "Search users by filters",
			description = "Search users using optional filters such as name, email, phone, and date of birth with pagination.",
			responses = {
				@ApiResponse(responseCode = "200", description = "Search results", content = @Content)
			}
	)
    @GetMapping("/search")
    public Flux<UserProjection> findByFilters(
    		@Parameter(description = "Name filter") @RequestParam(required = false) String name,
    		@Parameter(description = "Email filter") @RequestParam(required = false) String email,
    		@Parameter(description = "Phone filter") @RequestParam(required = false) String phone,
    		@Parameter(description = "Date of birth filter") @RequestParam(required = false) LocalDate dateOfBirth,
    		@Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
    		@Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size
    ) {
        return userService.search(name, email, phone, dateOfBirth, page, size);
    }
}

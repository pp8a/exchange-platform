package com.fintech.currency.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.currency.dto.postgres.AccountDTO;
import com.fintech.currency.dto.postgres.TransferRequest;
import com.fintech.currency.model.mongo.TransferLog;
import com.fintech.currency.service.api.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "Endpoints for managing account balance and transfers")
public class AccountController {
	private final AccountService accountService;
	
	@Operation(
		    summary = "Get current user balance",
		    description = "Returns the current balance of the authenticated user based on the X-USER-ID header.",
		    responses = {
		        @ApiResponse(responseCode = "200", description = "Balance retrieved successfully",
		            content = @Content(schema = @Schema(implementation = AccountDTO.class))),
		        @ApiResponse(responseCode = "404", description = "Account not found"),
		        @ApiResponse(responseCode = "500", description = "Internal server error")
		    }
	)	
	@GetMapping
	public Mono<AccountDTO> getMyBalance(
			@Parameter(description = "User ID extracted from token") @RequestHeader("X-USER-ID") UUID userId) {
		log.info("💰 [GET] get account balance for user: {}", userId);
		return accountService.getByUserId(userId);
	}	
	
	@Operation(
		    summary = "Transfer money to another user",
		    description = "Transfers a specified amount from the authenticated user to another user by userId.",
		    responses = {
		        @ApiResponse(responseCode = "200", description = "Transfer completed successfully",
		            content = @Content(schema = @Schema(implementation = String.class))),
		        @ApiResponse(responseCode = "400", description = "Invalid transfer request"),
		        @ApiResponse(responseCode = "404", description = "Sender or receiver not found"),
		        @ApiResponse(responseCode = "500", description = "Internal server error")
		    }
	)
	@PutMapping("/transfer")
	public Mono<String> transfer(
			@Parameter(description = "User ID of sender") @RequestHeader("X-USER-ID") UUID fromUserId,
	        @RequestBody @Valid TransferRequest request) {
		log.info("🔄 Transfer request: from {} to {}, amount {}", fromUserId, request.toUserId(), request.amount());
	    return accountService.getByUserId(fromUserId)
	        .flatMap(before -> 
	            accountService.transfer(fromUserId, request.toUserId(), request.amount())
	                .then(accountService.getFromDbByUserId(fromUserId)
	                    .map(after -> String.format(
	                        "✅ Transfer successful. Balance before: %s, after: %s",
	                        before.getBalance(), after.getBalance()
	                    ))
	                )
	        );
	}	
	
	@Operation(
		    summary = "Get transfer history",
		    description = "Returns all transfer operations for the given userId: both sent and received.",
		    responses = {
		        @ApiResponse(responseCode = "200", description = "Transfer history retrieved",
		            content = @Content(schema = @Schema(implementation = TransferLog.class))),
		        @ApiResponse(responseCode = "500", description = "Internal server error")
		    }
	)
	@GetMapping("/history")
	public Flux<TransferLog> getTransferHistory(
			@Parameter(description = "User ID of account owner") @RequestHeader("X-USER-ID") UUID userId) {
	    log.info("📜 [GET] transfer history for user: {}", userId);
	    return accountService.getTransferHistory(userId.toString());
	}
}

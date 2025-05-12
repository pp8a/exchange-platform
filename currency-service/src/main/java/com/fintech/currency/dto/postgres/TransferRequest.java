package com.fintech.currency.dto.postgres;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
	    @NotNull UUID toUserId,
	    @NotNull @DecimalMin("0.01") BigDecimal amount
	) {}

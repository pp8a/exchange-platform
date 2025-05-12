package com.fintech.currency.dto.postgres;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversionHistoryDTO {
	private UUID id;

    @NotNull
    private UUID transactionId;

    @NotBlank
    private String baseCurrency;

    @NotBlank
    private String targetCurrency;

    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull
    private BigDecimal exchangeRate;

    private BigDecimal spread = BigDecimal.ZERO;
    private BigDecimal commission = BigDecimal.ZERO;

    private LocalDateTime timestamp;
}

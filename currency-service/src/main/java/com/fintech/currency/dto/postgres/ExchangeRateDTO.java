package com.fintech.currency.dto.postgres;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
public class ExchangeRateDTO {
	private UUID id;

    @NotBlank
    private String baseCurrency;

    @NotBlank
    private String targetCurrency;

    @NotNull
    private BigDecimal rate;

    private LocalDateTime timestamp;

}

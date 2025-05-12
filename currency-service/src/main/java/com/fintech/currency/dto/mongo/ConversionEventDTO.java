package com.fintech.currency.dto.mongo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversionEventDTO {
	private String id;
    private String transactionId;
    private String clientId;
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;
    private BigDecimal exchangeRate;
    private LocalDateTime timestamp;

}

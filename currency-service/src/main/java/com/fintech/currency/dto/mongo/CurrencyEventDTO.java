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
public class CurrencyEventDTO {
	private String id;
    private String eventId;
    private String currencyCode;
    private BigDecimal oldRate;
    private BigDecimal newRate;
    private LocalDateTime timestamp;
}

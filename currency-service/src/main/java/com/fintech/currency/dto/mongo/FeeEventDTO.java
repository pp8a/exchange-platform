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
public class FeeEventDTO {
	private String id;
    private String transactionId;
    private BigDecimal feeAmount;
    private String currency;
    private LocalDateTime timestamp;
}

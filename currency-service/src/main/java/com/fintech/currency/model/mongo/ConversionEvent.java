package com.fintech.currency.model.mongo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conversion_events")
public class ConversionEvent {
	@Id
    private String id;

    private String transactionId;
    private String clientId;
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;
    private BigDecimal exchangeRate;
    private LocalDateTime timestamp;
}

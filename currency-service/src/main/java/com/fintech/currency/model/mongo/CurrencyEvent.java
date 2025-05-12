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
@Document(collection = "currency_events")
public class CurrencyEvent {
	@Id
    private String id;

    private String eventId;
    private String currencyCode;
    private BigDecimal oldRate;
    private BigDecimal newRate;
    private LocalDateTime timestamp;
}

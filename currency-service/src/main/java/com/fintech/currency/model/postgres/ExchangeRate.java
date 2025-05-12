package com.fintech.currency.model.postgres;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("exchange_rates")
public class ExchangeRate {
	@Id
    private UUID id;

    @Column("base_currency")
    private String baseCurrency; // Код валюты (ISO 4217)

    @Column("target_currency")
    private String targetCurrency; // Целевая валюта

    @Column("rate")
    private BigDecimal rate; // Курс обмена

    @Column("timestamp")
    private LocalDateTime timestamp; // Дата обновления курса

}

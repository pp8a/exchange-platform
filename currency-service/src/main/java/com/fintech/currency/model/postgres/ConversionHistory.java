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
@Table("conversion_history")
public class ConversionHistory {
	@Id
    private UUID id;

    @Column("transaction_id")
    private UUID transactionId; // Связь с general_ledger

    @Column("base_currency")
    private String baseCurrency;

    @Column("target_currency")
    private String targetCurrency;

    @Column("amount")
    private BigDecimal amount;

    @Column("exchange_rate")
    private BigDecimal exchangeRate;

    @Column("spread")
    private BigDecimal spread; // Разница в курсе

    @Column("commission")
    private BigDecimal commission; // Комиссия за обмен

    @Column("timestamp")
    private LocalDateTime timestamp;
}

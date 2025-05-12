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
@Table("general_ledger")
public class GeneralLedger {
	@Id
    private UUID id;

    @Column("transaction_id")
    private UUID transactionId; // Уникальный идентификатор транзакции

    @Column("base_currency")
    private String baseCurrency;

    @Column("target_currency")
    private String targetCurrency;

    @Column("amount")
    private BigDecimal amount; // Сумма транзакции

    @Column("exchange_rate")
    private BigDecimal exchangeRate; // Использованный курс обмена

    @Column("created_at")
    private LocalDateTime createdAt; // Дата создания

}

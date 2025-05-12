package com.fintech.currency.dto.postgres;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private UUID id;

    @NotNull
    private UUID userId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal balance;
    
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal initialBalance;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


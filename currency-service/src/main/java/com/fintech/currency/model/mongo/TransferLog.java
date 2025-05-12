package com.fintech.currency.model.mongo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("transfer_events")
public class TransferLog {
    @Id
    private String id;
    private String fromUserId;
    private String toUserId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}

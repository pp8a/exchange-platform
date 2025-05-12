package com.fintech.currency.model.postgres;

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
@Table("currency")
public class Currency {
	@Id
    private UUID id;

    @Column("code")
    private String code; // ISO 4217

    @Column("name")
    private String name;

    @Column("symbol")
    private String symbol;
}


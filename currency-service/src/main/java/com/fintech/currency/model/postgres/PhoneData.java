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
@Table("phone_data")
public class PhoneData {
    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("phone_number")
    private String phoneNumber;

    @Column("is_verified")
    private Boolean isVerified;
}

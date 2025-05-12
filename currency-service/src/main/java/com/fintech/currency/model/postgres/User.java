package com.fintech.currency.model.postgres;

import java.time.LocalDate;
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
@Table("users")
public class User {
    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("date_of_birth")
    private LocalDate dateOfBirth;

    @Column("password")
    private String password;

    @Column("created_at")
    private LocalDateTime createdAt;
}

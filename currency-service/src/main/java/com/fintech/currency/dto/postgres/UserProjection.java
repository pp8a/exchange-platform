package com.fintech.currency.dto.postgres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProjection {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
}

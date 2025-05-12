package com.fintech.currency.dto.postgres;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDataDTO {
    private UUID id;

    @NotNull
    private UUID userId;

    @Email
    @NotBlank
    private String email;

    private Boolean isVerified;
}


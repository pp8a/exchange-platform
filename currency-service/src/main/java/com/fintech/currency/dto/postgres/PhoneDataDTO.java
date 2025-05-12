package com.fintech.currency.dto.postgres;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDataDTO {
    private UUID id;

    @NotNull
    private UUID userId;

    @Pattern(regexp = "^\\d{11}$") // например 79207865432
    private String phoneNumber;

    private Boolean isVerified;
}

package com.fintech.currency.dto.postgres;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;

    @NotBlank
    private String name;

    private LocalDate dateOfBirth;

    @NotBlank
    @Size(min = 8)
    private String password;
    
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal initialBalance;
    
    @NotEmpty
    private List<PhoneDataDTO> phones;

    @NotEmpty
    private List<EmailDataDTO> emails;
}


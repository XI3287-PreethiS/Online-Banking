package com.online.banking.card.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CardRequestDto {

    @NotBlank(message = "Stakeholder name is required")
    private String stakeholderName;

    @NotBlank(message = "Card type is required")
    private String cardType;

    @NotBlank(message = "CVV is required")
    @Size(min = 3, max = 3, message = "CVV must be 3 digits")
    private String cvv;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than zero")
    private BigDecimal balance;
}

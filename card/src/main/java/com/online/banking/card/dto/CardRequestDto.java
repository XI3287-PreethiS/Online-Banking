package com.online.banking.card.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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


    private String cardNumber;

    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than zero")
    private BigDecimal balance;

}

package com.online.banking.account.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AccountRequestDto {
//    private Long id;

    @NotNull(message = "Account type cannot be null.")
    private String accountType;

    @NotNull(message = "User ID cannot be null.")
    private Long userId;

    @NotNull(message = "Balance cannot be null.")
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be greater than or equal to 0.00.")
    private BigDecimal balance;

}

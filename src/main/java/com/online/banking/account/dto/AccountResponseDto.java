package com.online.banking.account.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AccountResponseDto {
    private Long Id;
    private String accountNumber;
    private String accountType;
    private Boolean isActivated;
    private Long userId;
    private LocalDate accountStartDate;
    private BigDecimal balance;
}

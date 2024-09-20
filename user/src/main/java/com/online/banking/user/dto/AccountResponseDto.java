package com.online.banking.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
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
    private String userName;
}


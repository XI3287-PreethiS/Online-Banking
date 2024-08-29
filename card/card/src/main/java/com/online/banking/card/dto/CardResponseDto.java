package com.online.banking.card.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {

    private UUID id;
    private String cardNumber;
    private String stakeholderName;
    private String cvv;
    private LocalDate expiryDate;
    private LocalDateTime cardCreationTimestamp;
    private LocalDate creationDate;
    private Long accountId;
    private String cardType;
    private BigDecimal balance;
    private boolean isActive;
}

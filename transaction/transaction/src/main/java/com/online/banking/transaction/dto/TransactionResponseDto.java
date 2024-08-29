package com.online.banking.transaction.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private Long id;
    private String transactionId;
    private Long accountId;
    private Double amount;
    private String type;
    private LocalDateTime timestamp;
    private String description;
}

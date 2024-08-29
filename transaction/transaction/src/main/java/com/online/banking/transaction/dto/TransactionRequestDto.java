package com.online.banking.transaction.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionRequestDto {
    @NotNull
    private Long accountId;

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    @Size(max = 50)
    private String type;

    @Size(max = 50)
    private String description;

    private LocalDateTime timestamp;
}

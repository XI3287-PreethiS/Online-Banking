package com.online.banking.transaction.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId; // Unique ID for the transaction

    @Column(nullable = false)
    private Long accountId; // ID of the account related to this transaction

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String type; // Type of transaction (e.g., credit, debit)

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String description; // Description of the transaction

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
}

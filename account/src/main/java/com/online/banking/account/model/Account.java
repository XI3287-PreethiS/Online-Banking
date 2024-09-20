package com.online.banking.account.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;
//import com.online.banking.user.model.User;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, length = 11)
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "is_activated", nullable = false)
    private Boolean isActivated = false;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_start_date")
    private LocalDate accountStartDate;

    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;



//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
}



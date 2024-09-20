//package com.online.banking.card.model;
//
//import jakarta.persistence.*;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Table(name = "card")
//@Getter
//@Setter
//public class Card {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "card_number", unique = true, nullable = false, length = 16)
//    private String cardNumber; // 16-digit unique card number
//
//    @Column(name = "card_type", nullable = false)
//    private String cardType; // e.g., Credit, Debit
//
//    @Column(name = "is_activated", nullable = false)
//    private Boolean isActivated = false;
//
//    @Column(name = "user_id", nullable = false)
//    private Long userId; // Foreign key to user entity
//
//    @Column(name = "expiry_date")
//    private LocalDate expiryDate;
//
//    @Column(name = "balance", precision = 10, scale = 2)
//    private BigDecimal balance;
//
//    @Column(name = "is_deleted", nullable = false)
//    private Boolean isDeleted = false;
//
//}
//

package com.online.banking.card.model;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "account_number", unique = true, nullable = false, length = 16)
    private String cardNumber;

    @NotBlank
    private String stakeholderName;

    @NotNull
    @Size(min = 3, max = 3)
    private String cvv;

    @NotNull
    private LocalDate expiryDate;

    private LocalDateTime cardCreationTimestamp;

    private LocalDate creationDate;

    @NotNull
    private Long accountId;

    @NotBlank
    private String cardType;


    private BigDecimal balance;

    @Setter
    private boolean isActive;

    public boolean isActive(boolean b) {
        return isActive;
    }

}


package com.online.banking.card.repository;

import com.online.banking.card.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    Page<Card> findByStakeholderNameContainingAndCardType(String stakeholderName, String cardType, Pageable pageable);
}

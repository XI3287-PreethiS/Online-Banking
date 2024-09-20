package com.online.banking.card.service;

import com.online.banking.card.dto.CardRequestDto;
import com.online.banking.card.dto.CardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CardService {
    CardResponseDto createCard(CardRequestDto cardRequestDto);

    CardResponseDto updateCard(UUID id, CardRequestDto cardRequestDto);

    void deleteCard(UUID id);

    CardResponseDto getCardById(UUID id);

    Page<CardResponseDto> getAllCards(Pageable pageable);

    void addFunds(UUID id, double amount);

    void activateCard(UUID id);

    void deactivateCard(UUID id);

    Page<CardResponseDto> searchCards(String stakeholderName, String cardType, Pageable pageable);
}

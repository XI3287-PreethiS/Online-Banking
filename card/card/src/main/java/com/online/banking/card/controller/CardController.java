package com.online.banking.card.controller;

import com.online.banking.card.dto.CardRequestDto;
import com.online.banking.card.dto.CardResponseDto;
import com.online.banking.card.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Validated
public class CardController {

    private final CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<CardResponseDto> createCard(@Valid @RequestBody CardRequestDto cardRequestDto) {
        return ResponseEntity.ok(cardService.createCard(cardRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable UUID id, @Valid @RequestBody CardRequestDto cardRequestDto) {
        return ResponseEntity.ok(cardService.updateCard(id, cardRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDto> getCardById(@PathVariable UUID id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CardResponseDto>> getAllCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCards(pageable));
    }

    @PostMapping("/{id}/add-funds")
    public ResponseEntity<Void> addFunds(@PathVariable UUID id, @RequestParam @Positive(message = "Amount must be positive") double amount) {
        cardService.addFunds(id, amount);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateCard(@PathVariable UUID id) {
        cardService.activateCard(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCard(@PathVariable UUID id) {
        cardService.deactivateCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CardResponseDto>> searchCards(
            @RequestParam(required = false) String stakeholderName,
            @RequestParam(required = false) String cardType,
            Pageable pageable) {
        return ResponseEntity.ok(cardService.searchCards(stakeholderName, cardType, pageable));
    }
}

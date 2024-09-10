package com.online.banking.card.controller;

import com.online.banking.card.dto.CardRequestDto;
import com.online.banking.card.dto.CardResponseDto;
import com.online.banking.card.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.online.banking.card.dto.CardRequestDto.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private CardRequestDto cardRequestDto;
    private CardResponseDto cardResponseDto;
    private UUID cardId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cardId = UUID.randomUUID();

        cardRequestDto = builder()
//                .finalize(cardId)
                .cardNumber("1234567890123456")
                .stakeholderName("John Doe")
                .cvv("123")
                .expiryDate(LocalDate.parse("2025-12-31"))
                .cardType("Credit")
                .accountId(1L)
                .build();

        cardResponseDto = CardResponseDto.builder()
                .id(cardId)
                .cardNumber("1234567890123456")
                .stakeholderName("John Doe")
                .cardType("Credit")
                .build();
    }

    @Test
    public void testCreateCard() {
        when(cardService.createCard(cardRequestDto)).thenReturn(cardResponseDto);

        ResponseEntity<CardResponseDto> response = cardController.createCard(cardRequestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(cardResponseDto);
        verify(cardService).createCard(cardRequestDto);
    }

    @Test
    public void testUpdateCard() {
        when(cardService.updateCard(cardId, cardRequestDto)).thenReturn(cardResponseDto);

        ResponseEntity<CardResponseDto> response = cardController.updateCard(cardId, cardRequestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(cardResponseDto);
        verify(cardService).updateCard(cardId, cardRequestDto);
    }

    @Test
    public void testDeleteCard() {
        doNothing().when(cardService).deleteCard(cardId);

        ResponseEntity<Void> response = cardController.deleteCard(cardId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(cardService).deleteCard(cardId);
    }

    @Test
    public void testGetCardById() {
        when(cardService.getCardById(cardId)).thenReturn(cardResponseDto);

        ResponseEntity<CardResponseDto> response = cardController.getCardById(cardId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(cardResponseDto);
        verify(cardService).getCardById(cardId);
    }

    @Test
    public void testGetAllCards() {
        Page<CardResponseDto> cardResponseDtos = new PageImpl<>(List.of(cardResponseDto));
        when(cardService.getAllCards(any(Pageable.class))).thenReturn(cardResponseDtos);

        ResponseEntity<Page<CardResponseDto>> response = cardController.getAllCards(Pageable.unpaged());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent()).containsExactly(cardResponseDto);
        verify(cardService).getAllCards(any(Pageable.class));
    }


    @Test
    public void testAddFunds() {
        doNothing().when(cardService).addFunds(cardId, 100.0);

        ResponseEntity<Void> response = cardController.addFunds(cardId, 100.0);

        // Update the expected status to NO_CONTENT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(cardService).addFunds(cardId, 100.0);
    }


    @Test
    public void testActivateCard() {
        doNothing().when(cardService).activateCard(cardId);

        ResponseEntity<Void> response = cardController.activateCard(cardId);

        // Expecting 204 NO_CONTENT instead of 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(cardService).activateCard(cardId);
    }


    @Test
    public void testDeactivateCard() {
        doNothing().when(cardService).deactivateCard(cardId);

        ResponseEntity<Void> response = cardController.deactivateCard(cardId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(cardService).deactivateCard(cardId);
    }

    @Test
    public void testSearchCards() {
        Page<CardResponseDto> cardResponseDtos = new PageImpl<>(List.of(cardResponseDto));
        when(cardService.searchCards("John Doe", "Credit", Pageable.unpaged())).thenReturn(cardResponseDtos);

        ResponseEntity<Page<CardResponseDto>> response = cardController.searchCards("John Doe", "Credit", Pageable.unpaged());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent()).containsExactly(cardResponseDto);
        verify(cardService).searchCards("John Doe", "Credit", Pageable.unpaged());
    }
}


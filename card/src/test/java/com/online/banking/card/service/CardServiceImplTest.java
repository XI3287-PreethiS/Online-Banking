package com.online.banking.card.service;

import com.online.banking.card.dto.CardRequestDto;
import com.online.banking.card.dto.CardResponseDto;
import com.online.banking.card.exception.CardNotFoundException;
import com.online.banking.card.model.Card;
import com.online.banking.card.repository.CardRepository;
import com.online.banking.card.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card card;
    private CardRequestDto cardRequestDto;
    private CardResponseDto cardResponseDto;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        card = Card.builder()
                .id(UUID.randomUUID())
                .cardNumber("1234567890123456")
                .stakeholderName("John Doe")
                .cvv("123")
                .expiryDate(LocalDate.now().plusYears(1))
                .cardType("Credit")
                .balance(BigDecimal.valueOf(1000))
                .accountId(1L)
                .isActive(true)
                .build();

        cardResponseDto = CardResponseDto.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .stakeholderName(card.getStakeholderName())
                .cvv(card.getCvv())
                .expiryDate(card.getExpiryDate())
                .cardCreationTimestamp(card.getCardCreationTimestamp())
                .creationDate(card.getCreationDate())
                .accountId(card.getAccountId())
                .cardType(card.getCardType())
                .balance(card.getBalance())
                .isActive(card.isActive(true))
                .build();
    }

    @Test
    public void testCreateCard() {
        when(modelMapper.map(cardRequestDto, Card.class)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);
        when(modelMapper.map(card, CardResponseDto.class)).thenReturn(cardResponseDto);

        CardResponseDto result = cardService.createCard(cardRequestDto);

        assertThat(result).isEqualTo(cardResponseDto);
        verify(cardRepository).save(card);
    }

    @Test
    public void testUpdateCard() {
        UUID id = card.getId();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(modelMapper.map(cardRequestDto, Card.class)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);
        when(modelMapper.map(card, CardResponseDto.class)).thenReturn(cardResponseDto);

        CardResponseDto result = cardService.updateCard(id, cardRequestDto);

        assertThat(result).isEqualTo(cardResponseDto);
    }

    @Test
    public void testDeleteCard() {
        UUID id = card.getId();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));

        cardService.deleteCard(id);

        verify(cardRepository).delete(card);
    }

    @Test
    public void testGetCardById() {
        UUID id = card.getId();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(modelMapper.map(card, CardResponseDto.class)).thenReturn(cardResponseDto);

        CardResponseDto result = cardService.getCardById(id);

        assertThat(result).isEqualTo(cardResponseDto);
    }

    @Test
    public void testGetAllCards() {
        Page<Card> cardPage = new PageImpl<>(List.of(card));
        when(cardRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(cardPage);
        when(modelMapper.map(card, CardResponseDto.class)).thenReturn(cardResponseDto);

        Page<CardResponseDto> result = cardService.getAllCards(Pageable.unpaged());

        assertThat(result.getContent()).containsExactly(cardResponseDto);
    }


    @Test
    public void testAddFunds() {
        // Arrange
        UUID id = card.getId();
        double amount = 100;
        BigDecimal initialBalance = card.getBalance();
        BigDecimal expectedBalance = initialBalance.add(BigDecimal.valueOf(amount));

        // Mocking the repository
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        // Act
        cardService.addFunds(id, amount);

        // Assert
        assertThat(card.getBalance()).isEqualByComparingTo(expectedBalance);  // Check that balance is updated correctly
        verify(cardRepository, times(1)).save(card);  // Ensure the save method was called once
    }


    @Test
    public void testActivateCard() {
        UUID id = card.getId();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        card.setActive(true);
        when(cardRepository.save(card)).thenReturn(card);

        cardService.activateCard(id);

        assertThat(card.isActive(true)).isTrue();
    }

    @Test
    public void testDeactivateCard() {
        UUID id = card.getId();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        card.setActive(false);
        when(cardRepository.save(card)).thenReturn(card);

        cardService.deactivateCard(id);

        assertThat(card.isActive(true)).isFalse();
    }

    @Test
    public void testSearchCards() {
        Pageable pageable = Pageable.unpaged();
        Page<Card> cardPage = new PageImpl<>(List.of(card));
        when(cardRepository.findByStakeholderNameAndCardType("John", "Credit", pageable)).thenReturn(cardPage);
        when(modelMapper.map(card, CardResponseDto.class)).thenReturn(cardResponseDto);

        Page<CardResponseDto> result = cardService.searchCards("John", "Credit", pageable);

        assertThat(result.getContent()).containsExactly(cardResponseDto);
    }

    @Test
    public void testCardNotFoundException() {
        UUID id = card.getId();
        when(cardRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(id));
    }
}


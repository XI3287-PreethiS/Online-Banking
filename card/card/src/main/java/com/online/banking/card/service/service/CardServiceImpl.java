package com.online.banking.card.service;

import com.online.banking.card.dto.CardRequestDto;
import com.online.banking.card.dto.CardResponseDto;
import com.online.banking.card.exception.CardNotFoundException;
import com.online.banking.card.model.Card;
import com.online.banking.card.repository.CardRepository;
import com.online.banking.card.util.Constants;
import com.online.banking.card.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Override
    public CardResponseDto createCard(CardRequestDto cardRequestDto) {
        validationUtil.validateCardRequest(cardRequestDto);
        Card card = modelMapper.map(cardRequestDto, Card.class);
        card.setCardNumber(UUID.randomUUID().toString());
        card.isActive(true);
        card = cardRepository.save(card);
        return modelMapper.map(card, CardResponseDto.class);

    }


    @Override
    public CardResponseDto updateCard(UUID id, CardRequestDto cardRequestDto) {
        validationUtil.validateCardRequest(cardRequestDto);
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(Constants.CARD_NOT_FOUND));
        modelMapper.map(cardRequestDto, card);
        card = cardRepository.save(card);
        return modelMapper.map(card, CardResponseDto.class);
    }

    @Override
    public void deleteCard(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(Constants.CARD_NOT_FOUND));
        cardRepository.delete(card);
        System.out.println(Constants.CARD_DELETE_SUCCESS);
    }

    @Override
    public CardResponseDto getCardById(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(Constants.CARD_NOT_FOUND));
        return modelMapper.map(card, CardResponseDto.class);

    }

    @Override
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(card -> modelMapper.map(card, CardResponseDto.class));
    }

    @Override
    public void addFunds(UUID id, double amount) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(Constants.CARD_NOT_FOUND));
        card.setBalance(card.getBalance().add(BigDecimal.valueOf(amount)));
        cardRepository.save(card);
        System.out.println(Constants.ADD_FUNDS_SUCCESS);
    }

    @Override
    public void activateCard(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(Constants.CARD_NOT_FOUND));
        card.setActive(true);
        cardRepository.save(card);
        System.out.println(Constants.CARD_ACTIVATE_SUCCESS);
    }

    @Override
    public void deactivateCard(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(Constants.CARD_NOT_FOUND));
        card.setActive(false);
        cardRepository.save(card);
        System.out.println(Constants.CARD_DEACTIVATE_SUCCESS);
    }

    @Override
    public Page<CardResponseDto> searchCards(String stakeholderName, String cardType, Pageable pageable) {
        return cardRepository.findByStakeholderNameAndCardType(stakeholderName, cardType, pageable)
                .map(card -> modelMapper.map(card, CardResponseDto.class));
    }
}

package com.online.banking.user.client;

import com.online.banking.user.dto.CardRequestDto;
import com.online.banking.user.dto.CardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CardClient {

    private final RestTemplate restTemplate;

    @Value("${card.service.url}")
    private String cardServiceUrl;


    public CardResponseDto createCard(CardRequestDto cardRequestDto) {

        cardRequestDto.isActive(false);
        return restTemplate.postForObject(
                cardServiceUrl + "/create",
                cardRequestDto,
                CardResponseDto.class
        );
    }
}

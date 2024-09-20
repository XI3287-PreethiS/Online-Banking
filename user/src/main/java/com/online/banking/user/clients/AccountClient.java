package com.online.banking.user.clients;

import com.online.banking.user.dto.AccountRequestDto;
import com.online.banking.user.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AccountClient {

    private final RestTemplate restTemplate;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    public AccountResponseDto createAccount(AccountRequestDto accountRequestDto) {

        return restTemplate.postForEntity(
                accountServiceUrl + "/create",
                accountRequestDto,
                AccountResponseDto.class
        ).getBody();
    }
}

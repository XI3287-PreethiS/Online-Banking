package com.online.banking.account.service;

import com.online.banking.account.dto.AccountRequestDto;
import com.online.banking.account.dto.AccountResponseDto;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountResponseDto createAccount(AccountRequestDto requestDto);
    void activateDeactivateAccount(Long id, Boolean isActive);
    void deleteAccount(long id);
    AccountResponseDto changeAccountType(long id, String accountType);
    Page<AccountResponseDto> getAllAccounts(int page, int size, String sortBy, String direction);
    AccountResponseDto getAccountById(long id);
    AccountResponseDto getAccountByUserId(long userId);
    List<AccountResponseDto> searchAccounts(String accountNumberPrefix);
    AccountResponseDto addFund(String accountNumber, BigDecimal amount);
    AccountResponseDto withdrawAmount(String accountNumber, BigDecimal amount);

    AccountResponseDto getAccountBalance(String accountNumber);
}

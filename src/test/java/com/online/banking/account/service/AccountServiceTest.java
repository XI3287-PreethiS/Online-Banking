package com.online.banking.account.service;

import com.online.banking.account.dto.AccountRequestDto;
import com.online.banking.account.dto.AccountResponseDto;
import com.online.banking.account.exception.AccountNotFoundException;
import com.online.banking.account.model.Account;
import com.online.banking.account.repository.AccountRepository;
import com.online.banking.account.util.ConstantUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountRequestDto requestDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("12345678901");
        account.setAccountType("savings");
        account.setUserId(1L);
        account.setBalance(new BigDecimal("5000.00"));
        account.setIsActivated(true);

        requestDto = new AccountRequestDto();
        requestDto.setAccountType("savings");
        requestDto.setUserId(1L);
        requestDto.setBalance(new BigDecimal("5000.00"));
    }

    @Test
    public void testCreateAccount() {
        when(modelMapper.map(requestDto, Account.class)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(modelMapper.map(account, AccountResponseDto.class)).thenReturn(new AccountResponseDto());

        AccountResponseDto responseDto = accountService.createAccount(requestDto);

        assertThat(responseDto).isNotNull();
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testDeleteAccount() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).save(account);
        assertThat(account.getIsDeleted()).isTrue();
    }

    @Test
    public void testDeleteAccountNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.deleteAccount(1L))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage(ConstantUtil.ACCOUNT_NOT_FOUND_MESSAGE);
    }


}

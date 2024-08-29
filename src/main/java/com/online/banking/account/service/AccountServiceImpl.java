package com.online.banking.account.service;

import com.online.banking.account.dto.AccountRequestDto;
import com.online.banking.account.dto.AccountResponseDto;
import com.online.banking.account.exception.AccountNotFoundException;
import com.online.banking.account.model.Account;
import com.online.banking.account.repository.AccountRepository;
import com.online.banking.account.util.ConstantUtil;
import com.online.banking.account.util.UUIDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public AccountResponseDto createAccount(AccountRequestDto requestDto) {
        Account account = modelMapper.map(requestDto, Account.class);
        account.setAccountNumber(UUIDGenerator.generateUniqueAccountNumber());
        account.setAccountStartDate(LocalDate.now());
        account.setIsActivated(requestDto.getBalance().compareTo(new BigDecimal("1000.00")) >= 0);
        accountRepository.save(account);
        return modelMapper.map(account, AccountResponseDto.class);
    }

    @Override
    public void deleteAccount(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ConstantUtil.ACCOUNT_NOT_FOUND_MESSAGE));
        account.setIsDeleted(true);
        accountRepository.save(account);
    }

    @Override
    public AccountResponseDto changeAccountType(long id, String accountType) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ConstantUtil.ACCOUNT_NOT_FOUND_MESSAGE));
        account.setAccountType(accountType);
        accountRepository.save(account);
        return modelMapper.map(account, AccountResponseDto.class);
    }

    @Override
    public void activateDeactivateAccount(Long id, Boolean isActive) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ConstantUtil.ACCOUNT_NOT_FOUND_MESSAGE));
        account.setIsActivated(isActive);
        accountRepository.save(account);
    }

    @Override
    public Page<AccountResponseDto> getAllAccounts(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.map(account -> modelMapper.map(account, AccountResponseDto.class));
    }

    @Override
    public AccountResponseDto getAccountById(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ConstantUtil.ACCOUNT_NOT_FOUND_MESSAGE));
        return modelMapper.map(account, AccountResponseDto.class);
    }

    @Override
    public AccountResponseDto getAccountByUserId(long userId) {
        Account account = accountRepository.findByUserId(userId).stream().findFirst()
                .orElseThrow(() -> new AccountNotFoundException(ConstantUtil.ACCOUNT_NOT_FOUND_MESSAGE));
        return modelMapper.map(account, AccountResponseDto.class);
    }

    @Override
    public List<AccountResponseDto> searchAccounts(String accountNumberPrefix) {
        return accountRepository.findByAccountNumberStartingWith(accountNumberPrefix).stream()
                .map(account -> modelMapper.map(account, AccountResponseDto.class))
                .collect(Collectors.toList());
    }
}


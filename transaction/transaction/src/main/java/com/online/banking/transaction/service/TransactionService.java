package com.online.banking.transaction.service;

import com.online.banking.transaction.dto.TransactionRequestDto;
import com.online.banking.transaction.dto.TransactionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TransactionService {
    TransactionResponseDto createTransaction(TransactionRequestDto requestDto);
    TransactionResponseDto getTransactionById(Long id);
    Page<TransactionResponseDto> getAllTransactions(Pageable pageable);
    Page<TransactionResponseDto> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    void deleteTransaction(Long id);
}

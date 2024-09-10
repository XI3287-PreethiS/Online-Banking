package com.online.banking.transaction.service;

import com.online.banking.transaction.dto.TransactionRequestDto;
import com.online.banking.transaction.dto.TransactionResponseDto;
import com.online.banking.transaction.exception.TransactionNotFoundException;
import com.online.banking.transaction.model.Transaction;
import com.online.banking.transaction.repository.TransactionRepository;
import com.online.banking.transaction.util.ConstantsUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_ShouldReturnTransactionResponseDto() {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        requestDto.setAccountId(1L);
        requestDto.setAmount(100.0);
        requestDto.setType("credit");
        requestDto.setDescription("Test transaction");
        requestDto.setTimestamp(LocalDateTime.now());

        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());

        when(modelMapper.map(requestDto, Transaction.class)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(modelMapper.map(transaction, TransactionResponseDto.class))
                .thenReturn(new TransactionResponseDto());

        TransactionResponseDto responseDto = transactionService.createTransaction(requestDto);

        assertNotNull(responseDto);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void getTransactionById_ShouldReturnTransactionResponseDto() {
        Long id = 1L;
        Transaction transaction = new Transaction();
        TransactionResponseDto responseDto = new TransactionResponseDto();

        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
        when(modelMapper.map(transaction, TransactionResponseDto.class)).thenReturn(responseDto);

        TransactionResponseDto result = transactionService.getTransactionById(id);

        assertNotNull(result);
        verify(transactionRepository, times(1)).findById(id);
    }

    @Test
    void getTransactionById_ShouldThrowExceptionIfNotFound() {
        Long id = 1L;
        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        TransactionNotFoundException thrown = assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.getTransactionById(id),
                ConstantsUtil.TRANSACTION_NOT_FOUND + id
        );

        assertEquals(ConstantsUtil.TRANSACTION_NOT_FOUND + id, thrown.getMessage());
    }

    @Test
    void getAllTransactions_ShouldReturnPagedTransactions() {
        Pageable pageable = Pageable.unpaged();
        Transaction transaction = new Transaction();
        TransactionResponseDto responseDto = new TransactionResponseDto();

        Page<Transaction> transactionsPage = new PageImpl<>(List.of(transaction));
        when(transactionRepository.findAll(pageable)).thenReturn(transactionsPage);
        when(modelMapper.map(transaction, TransactionResponseDto.class)).thenReturn(responseDto);

        Page<TransactionResponseDto> result = transactionService.getAllTransactions(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository, times(1)).findAll(pageable);
    }

    @Test
    void getTransactionsByDateRange_ShouldReturnPagedTransactions() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        Pageable pageable = Pageable.unpaged();
        Transaction transaction = new Transaction();
        TransactionResponseDto responseDto = new TransactionResponseDto();

        Page<Transaction> transactionsPage = new PageImpl<>(List.of(transaction));
        when(transactionRepository.findByTransactionDateBetween(startDate, endDate, pageable))
                .thenReturn(transactionsPage);
        when(modelMapper.map(transaction, TransactionResponseDto.class)).thenReturn(responseDto);

        Page<TransactionResponseDto> result = transactionService.getTransactionsByDateRange(startDate, endDate, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository, times(1)).findByTransactionDateBetween(startDate, endDate, pageable);
    }

    @Test
    void deleteTransaction_ShouldNotThrowException() {
        Long id = 1L;
        when(transactionRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> transactionService.deleteTransaction(id));
        verify(transactionRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteTransaction_ShouldThrowExceptionIfNotFound() {
        Long id = 1L;
        when(transactionRepository.existsById(id)).thenReturn(false);

        TransactionNotFoundException thrown = assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.deleteTransaction(id),
                ConstantsUtil.TRANSACTION_NOT_FOUND + id
        );

        assertEquals(ConstantsUtil.TRANSACTION_NOT_FOUND + id, thrown.getMessage());
    }
}

package com.online.banking.transaction.controller;

import com.online.banking.transaction.dto.TransactionRequestDto;
import com.online.banking.transaction.dto.TransactionResponseDto;
import com.online.banking.transaction.exception.TransactionNotFoundException;
import com.online.banking.transaction.service.TransactionService;
import com.online.banking.transaction.util.ConstantsUtil;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_ShouldReturnCreated() {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        TransactionResponseDto responseDto = new TransactionResponseDto();

        when(transactionService.createTransaction(requestDto)).thenReturn(responseDto);

        ResponseEntity<?> responseEntity = transactionController.createTransaction(requestDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void createTransaction_ShouldReturnBadRequestForInvalidInput() {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        when(transactionService.createTransaction(requestDto))
                .thenThrow(new IllegalArgumentException(ConstantsUtil.INVALID_INPUT));

        ResponseEntity<?> responseEntity = transactionController.createTransaction(requestDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(ConstantsUtil.INVALID_INPUT, responseEntity.getBody());
    }

    @Test
    void createTransaction_ShouldReturnInternalServerErrorForFailure() {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        when(transactionService.createTransaction(requestDto))
                .thenThrow(new RuntimeException(ConstantsUtil.TRANSACTION_CREATION_FAILED));

        ResponseEntity<?> responseEntity = transactionController.createTransaction(requestDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(ConstantsUtil.TRANSACTION_CREATION_FAILED, responseEntity.getBody());
    }

    @Test
    void getTransactionById_ShouldReturnTransaction() {
        Long id = 1L;
        TransactionResponseDto responseDto = new TransactionResponseDto();

        when(transactionService.getTransactionById(id)).thenReturn(responseDto);

        ResponseEntity<?> responseEntity = transactionController.getTransactionById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void getTransactionById_ShouldReturnNotFound() {
        Long id = 1L;
        when(transactionService.getTransactionById(id))
                .thenThrow(new TransactionNotFoundException(ConstantsUtil.TRANSACTION_NOT_FOUND + id));

        ResponseEntity<?> responseEntity = transactionController.getTransactionById(id);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ConstantsUtil.TRANSACTION_NOT_FOUND + id, responseEntity.getBody());
    }

    @Test
    void getAllTransactions_ShouldReturnPagedTransactions() {
        Pageable pageable = Pageable.unpaged();
        TransactionResponseDto responseDto = new TransactionResponseDto();

        Page<TransactionResponseDto> responsePage = new PageImpl<>(List.of(responseDto));
        when(transactionService.getAllTransactions(pageable)).thenReturn(responsePage);

        ResponseEntity<Page<TransactionResponseDto>> responseEntity = transactionController.getAllTransactions(pageable);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void findByTransactionDateBetween_ShouldReturnPagedTransactions() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        Pageable pageable = Pageable.unpaged();
        TransactionResponseDto responseDto = new TransactionResponseDto();

        Page<TransactionResponseDto> responsePage = new PageImpl<>(List.of(responseDto));
        when(transactionService.getTransactionsByDateRange(startDate, endDate, pageable)).thenReturn(responsePage);

        ResponseEntity<?> responseEntity = transactionController.findByTransactionDateBetween(startDate, endDate, pageable);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void deleteTransaction_ShouldReturnNoContent() {
        Long id = 1L;
        doNothing().when(transactionService).deleteTransaction(id);

        ResponseEntity<?> responseEntity = transactionController.deleteTransaction(id);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void deleteTransaction_ShouldReturnNotFound() {
        Long id = 1L;
        doThrow(new TransactionNotFoundException(ConstantsUtil.TRANSACTION_NOT_FOUND + id))
                .when(transactionService).deleteTransaction(id);

        ResponseEntity<?> responseEntity = transactionController.deleteTransaction(id);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ConstantsUtil.TRANSACTION_NOT_FOUND + id, responseEntity.getBody());
    }
}


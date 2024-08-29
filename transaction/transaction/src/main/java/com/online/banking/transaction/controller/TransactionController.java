//package com.online.banking.transaction.controller;
//
//import com.online.banking.transaction.dto.TransactionRequestDto;
//import com.online.banking.transaction.dto.TransactionResponseDto;
//import com.online.banking.transaction.exception.TransactionNotFoundException;
//import com.online.banking.transaction.service.TransactionService;
//import com.online.banking.transaction.util.ConstantsUtil;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//
//@RestController
//@RequestMapping("/api/v1/transactions")
//@RequiredArgsConstructor
//public class TransactionController {
//    private final TransactionService transactionService;
//
//    @PostMapping("/create")
//    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto requestDto) {
//        TransactionResponseDto responseDto = transactionService.createTransaction(requestDto);
//        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
//        try {
//            TransactionResponseDto responseDto = transactionService.getTransactionById(id);
//            return ResponseEntity.ok(responseDto);
//        } catch (TransactionNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactions(Pageable pageable) {
//        Page<TransactionResponseDto> transactions = transactionService.getAllTransactions(pageable);
//        return ResponseEntity.ok(transactions);
//    }
//
//    @GetMapping("/date-range")
//    public ResponseEntity<Page<TransactionResponseDto>> findByTransactionDateBetween(
//            @RequestParam("startDate") LocalDateTime startDate,
//            @RequestParam("endDate") LocalDateTime endDate,
//            Pageable pageable) {
//        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByDateRange(startDate, endDate, pageable);
//        return ResponseEntity.ok(transactions);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
//        try {
//            transactionService.deleteTransaction(id);
//            return ResponseEntity.noContent().build();
//        } catch (TransactionNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
//}

package com.online.banking.transaction.controller;

import com.online.banking.transaction.dto.TransactionRequestDto;
import com.online.banking.transaction.dto.TransactionResponseDto;
import com.online.banking.transaction.exception.TransactionNotFoundException;
import com.online.banking.transaction.service.TransactionService;
import com.online.banking.transaction.util.ConstantsUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequestDto requestDto) {
        try {
            TransactionResponseDto responseDto = transactionService.createTransaction(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ConstantsUtil.INVALID_INPUT);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ConstantsUtil.TRANSACTION_CREATION_FAILED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        try {
            TransactionResponseDto responseDto = transactionService.getTransactionById(id);
            return ResponseEntity.ok(responseDto);
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ConstantsUtil.TRANSACTION_NOT_FOUND + id);
        }
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactions(Pageable pageable) {
        Page<TransactionResponseDto> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/date-range")
    public ResponseEntity<?> findByTransactionDateBetween(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            Pageable pageable) {
        try {
            Page<TransactionResponseDto> transactions = transactionService.getTransactionsByDateRange(startDate, endDate, pageable);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ConstantsUtil.INVALID_INPUT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ConstantsUtil.TRANSACTION_NOT_FOUND + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ConstantsUtil.TRANSACTION_DELETION_FAILED);
        }
    }
}

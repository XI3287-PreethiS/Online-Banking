package com.online.banking.transaction.service;

import com.online.banking.transaction.dto.TransactionRequestDto;
import com.online.banking.transaction.dto.TransactionResponseDto;
import com.online.banking.transaction.exception.TransactionNotFoundException;
import com.online.banking.transaction.model.Transaction;
import com.online.banking.transaction.repository.TransactionRepository;
import com.online.banking.transaction.util.ConstantsUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public TransactionResponseDto createTransaction(TransactionRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException(ConstantsUtil.INVALID_INPUT);
        }
        Transaction transaction = modelMapper.map(requestDto, Transaction.class);
        transaction.setTransactionId(UUID.randomUUID().toString());
        try {
            transaction = transactionRepository.save(transaction);
            return modelMapper.map(transaction, TransactionResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException(ConstantsUtil.TRANSACTION_CREATION_FAILED, e);
        }
    }

    @Override
    public TransactionResponseDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(ConstantsUtil.TRANSACTION_NOT_FOUND + id));
        return modelMapper.map(transaction, TransactionResponseDto.class);
    }

    @Override
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(transaction -> modelMapper.map(transaction, TransactionResponseDto.class));
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(ConstantsUtil.INVALID_INPUT);
        }
        return transactionRepository.findByTransactionDateBetween(startDate, endDate, pageable)
                .map(transaction -> modelMapper.map(transaction, TransactionResponseDto.class));
    }

    @Override
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException(ConstantsUtil.TRANSACTION_NOT_FOUND + id);
        }
        try {
            transactionRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(ConstantsUtil.TRANSACTION_DELETION_FAILED, e);
        }
    }
}

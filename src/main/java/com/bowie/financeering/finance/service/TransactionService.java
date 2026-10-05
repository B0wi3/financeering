package com.bowie.financeering.finance.service;

import com.bowie.financeering.finance.DTO.TransactionCreateDTO;
import com.bowie.financeering.finance.DTO.TransactionResponseDTO;
import com.bowie.financeering.finance.model.Transaction;
import com.bowie.financeering.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponseDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);

        return toResponseDto(transaction);
    }

    public List<TransactionResponseDTO> getAllTransactions(String userSub) {
        List<Transaction> transactions = transactionRepository.findByUserSubOrderByCreatedAtDesc(userSub);

        return transactions.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO createTransaction(TransactionCreateDTO dto, String userSub) {
        Transaction transaction = new Transaction(
                dto.getAmount(),
                dto.getCurrency(),
                dto.getTransactionType(),
                dto.getCategory(),
                userSub
        );

        transactionRepository.save(transaction);
        return toResponseDto(transaction);
    }

    private TransactionResponseDTO toResponseDto(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionType(),
                transaction.getCategory(),
                transaction.getCreatedAt()
        );
    }
}

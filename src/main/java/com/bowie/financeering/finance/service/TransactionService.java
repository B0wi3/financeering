package com.bowie.financeering.finance.service;

import com.bowie.financeering.finance.DTO.TransactionCreateDTO;
import com.bowie.financeering.finance.DTO.TransactionResponseDTO;
import com.bowie.financeering.finance.DTO.TransactionUpdateDTO;
import com.bowie.financeering.finance.model.Transaction;
import com.bowie.financeering.finance.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponseDTO getTransactionById(Long id, String userSub) throws AccessDeniedException {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Transaction not found with id: " + id)
        );

        if (!transaction.getUserSub().equals(userSub)) {
            throw new AccessDeniedException("You don't have permission to access this transaction");
        }

        return toResponseDto(transaction);
    }

    public List<TransactionResponseDTO> getAllTransactions(String userSub) {
        List<Transaction> transactions = transactionRepository.findByUserSubOrderByCreatedAtDesc(userSub);

        return transactions.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
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

    @Transactional
    public TransactionResponseDTO updateTransaction(Long id, TransactionUpdateDTO dto, String userSub) throws AccessDeniedException {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Transaction not found with id: " + id)
        );

        if (!transaction.getUserSub().equals(userSub)) {
            throw new AccessDeniedException("You don't have permission to modify this transaction");
        }

        transaction.setAmount(dto.getAmount());
        transaction.setCurrency(dto.getCurrency());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setCategory(dto.getCategory());

        transactionRepository.save(transaction);

        return toResponseDto(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id, String userSub) throws AccessDeniedException {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Transaction not found with id: " + id)
        );

        if (!transaction.getUserSub().equals(userSub)) {
            throw new AccessDeniedException("You don't have permission to delete this transaction");
        }

        transactionRepository.delete(transaction);
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

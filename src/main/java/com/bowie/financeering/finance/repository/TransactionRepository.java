package com.bowie.financeering.finance.repository;

import com.bowie.financeering.finance.model.ENUM.TransactionType;
import com.bowie.financeering.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findById(Long id);
    List<Transaction> findByTransactionType(TransactionType type, String userSub);
    List<Transaction> findByCategory(String category, String userSub);
    List<Transaction> findByUserSubOrderByCreatedAtDesc(String userSub);
}

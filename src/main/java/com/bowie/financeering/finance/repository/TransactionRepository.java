package com.bowie.financeering.finance.repository;

import com.bowie.financeering.finance.model.ENUM.TransactionType;
import com.bowie.financeering.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserSub(String userSub);
    List<Transaction> findByTransactionType(TransactionType type);
    List<Transaction> findByCategory(String category);
    List<Transaction> findByUserSubOrderByCreatedAtDesc(String userSub);
}

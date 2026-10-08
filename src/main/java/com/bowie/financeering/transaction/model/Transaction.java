package com.bowie.financeering.transaction.model;

import com.bowie.financeering.transaction.model.ENUM.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    @Column(name = "amount")
    private BigDecimal amount;

    @NotNull
    @Pattern(regexp = "(BRL|USD|EUR)")
    @Column(name = "currency", length = 3)
    private String currency;

    @NotNull
    @Column(name = "transactionType")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @NotBlank
    @Column(name = "category")
    private String category;

    @NotBlank
    @Column(name = "user_sub")
    private String userSub;

    @NotNull
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, String currency, TransactionType transactionType, String category, String userSub) {
        this.amount = amount;
        this.currency = currency;
        this.transactionType = TransactionType.valueOf(transactionType.name().toUpperCase());
        this.category = category;
        this.userSub = userSub;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = TransactionType.valueOf(transactionType.name().toUpperCase());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserSub() {
        return userSub;
    }

    public void setUserSub(String userSub) {
        this.userSub = userSub;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
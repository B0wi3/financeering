package com.bowie.financeering.finance.DTO;

import com.bowie.financeering.finance.model.ENUM.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransactionCreateDTO {

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @Pattern(regexp = "[A-Z]{3}")
    private String currency;

    @NotNull
    private TransactionType transactionType;

    @NotBlank
    private String category;

    public TransactionCreateDTO() {
    }

    public TransactionCreateDTO(BigDecimal amount, String currency, TransactionType transactionType, String category, String userSub) {
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.category = category;
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
        this.transactionType = transactionType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

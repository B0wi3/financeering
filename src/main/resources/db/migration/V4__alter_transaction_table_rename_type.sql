ALTER TABLE transaction
    CHANGE COLUMN transactionType transaction_type ENUM('expense', 'income') NOT NULL;
ALTER TABLE transaction
    MODIFY COLUMN transaction_type ENUM('EXPENSE', 'INCOME') NOT NULL;
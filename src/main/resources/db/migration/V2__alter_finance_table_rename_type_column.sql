ALTER TABLE finance
CHANGE COLUMN type transactionType ENUM('expense', 'income') NOT NULL;
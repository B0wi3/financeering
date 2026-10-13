CREATE TABLE transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    amount DECIMAL(19,4) NOT NULL,
    currency CHAR(3) NOT NULL,
    transaction_type ENUM('EXPENSE', 'INCOME') NOT NULL,
    category VARCHAR(36) NOT NULL,
    user_sub VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
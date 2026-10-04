CREATE TABLE finance (
    id INT PRIMARY KEY AUTO_INCREMENT,
    amount DECIMAL(19,4) NOT NULL,
    currency CHAR(3) NOT NULL,
    type ENUM('expense', 'income') NOT NULL,
    category VARCHAR(36) NOT NULL,
    user_sub VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
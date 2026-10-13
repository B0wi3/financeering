CREATE TABLE users (
    user_sub VARCHAR(36) PRIMARY KEY,
    currency CHAR(3) NOT NULL,
    locale VARCHAR(10) NOT NULL
);
CREATE TABLE users (
    user_sub VARCHAR(36) PRIMARY KEY,
    currency VARCHAR(3) NOT NULL,
    locale VARCHAR(10) NOT NULL
);
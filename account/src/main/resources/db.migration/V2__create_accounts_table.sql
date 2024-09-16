CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(11) NOT NULL UNIQUE,
    account_type VARCHAR(50) NOT NULL,
    is_activated BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    account_start_date DATE,
    balance DECIMAL(10, 2),
    is_deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE cards (
    id CHAR(36) PRIMARY KEY, -- UUID
    card_number VARCHAR(16) NOT NULL UNIQUE,
    stakeholder_name VARCHAR(255) NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    expiry_date DATE NOT NULL,
    card_creation_timestamp DATETIME,
    creation_date DATE,
    account_id BIGINT NOT NULL,
    card_type VARCHAR(50) NOT NULL,
    balance DECIMAL(10, 2),
    is_active BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

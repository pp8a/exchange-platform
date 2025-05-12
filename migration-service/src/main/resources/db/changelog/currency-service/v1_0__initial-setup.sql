-- Таблица пользователей
-- changeset user:create-users-table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(500) NOT NULL,
    date_of_birth DATE,
    password VARCHAR(500) NOT NULL CHECK (char_length(password) >= 8),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

-- Email-адреса
-- changeset user:create-email-data-table
CREATE TABLE IF NOT EXISTS email_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL UNIQUE,
    is_verified BOOLEAN DEFAULT FALSE
);

-- Телефоны
-- changeset user:create-phone-data-table
CREATE TABLE IF NOT EXISTS phone_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    is_verified BOOLEAN DEFAULT FALSE
);

-- Аккаунты
-- changeset user:create-account-table
CREATE TABLE IF NOT EXISTS account (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    balance DECIMAL(19,2) NOT NULL CHECK (balance >= 0),
    initial_balance DECIMAL(19,2) CHECK (initial_balance >= 0),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

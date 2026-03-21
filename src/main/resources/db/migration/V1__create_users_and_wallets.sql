-- 1. Activamos la extensión para UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. Tabla de Monedas (Referencia)
CREATE TABLE currencies (
                            id CHAR(3) PRIMARY KEY, -- 'USD', 'MXN'
                            name VARCHAR(50) NOT NULL,
                            created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla de Usuarios
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       lastname VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabla de Wallet
CREATE TABLE wallets (
                         id UUID PRIMARY KEY,
                         user_id UUID UNIQUE NOT NULL,
                         currency_id CHAR(3) NOT NULL,
                         balance NUMERIC(19, 4) NOT NULL DEFAULT 0 CHECK (balance >= 0),
                         created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                         CONSTRAINT fk_currency FOREIGN KEY (currency_id) REFERENCES currencies(id)
);
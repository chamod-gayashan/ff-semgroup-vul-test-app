-- =============================================================
-- Semgroup App – Database Schema
-- =============================================================

CREATE DATABASE IF NOT EXISTS semgroup_app_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE semgroup_app_db;

-- -------------------------------------------------------------
-- Users
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
  id          BIGINT        AUTO_INCREMENT PRIMARY KEY,
  full_name   VARCHAR(100)  NOT NULL,
  email       VARCHAR(255)  NOT NULL UNIQUE,
  password    VARCHAR(255)  NOT NULL,
  role        ENUM('ROLE_ADMIN','ROLE_USER') NOT NULL DEFAULT 'ROLE_USER',
  is_active   TINYINT(1)    NOT NULL DEFAULT 1,
  created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- -------------------------------------------------------------
-- Accounts
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS accounts (
  id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
  owner_id    BIGINT          NOT NULL,
  owner_name  VARCHAR(100)    NOT NULL,
  account_no  VARCHAR(20)     NOT NULL UNIQUE,
  balance     DECIMAL(15, 2)  NOT NULL DEFAULT 0.00,
  created_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_account_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

-- -------------------------------------------------------------
-- Transactions
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS transactions (
  id            BIGINT          AUTO_INCREMENT PRIMARY KEY,
  from_account  BIGINT          NOT NULL,
  to_account    BIGINT          NOT NULL,
  owner_name    VARCHAR(100)    NOT NULL,
  amount        DECIMAL(15, 2)  NOT NULL,
  note          TEXT,
  created_at    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_txn_from FOREIGN KEY (from_account) REFERENCES accounts (id),
  CONSTRAINT fk_txn_to   FOREIGN KEY (to_account)   REFERENCES accounts (id)
);

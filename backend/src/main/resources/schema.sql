-- ============================================================
-- VulnBank – schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS vulnbank_db;
USE vulnbank_db;

CREATE TABLE IF NOT EXISTS users (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name   VARCHAR(100) NOT NULL,
  email       VARCHAR(255) UNIQUE NOT NULL,
  password    VARCHAR(255) NOT NULL,
  role        ENUM('ROLE_USER','ROLE_ADMIN') DEFAULT 'ROLE_USER',
  created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  owner_id     BIGINT NOT NULL,
  owner_name   VARCHAR(100) NOT NULL,
  account_no   VARCHAR(20) UNIQUE NOT NULL,
  balance      DECIMAL(15,2) DEFAULT 0.00,
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS transactions (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  from_account  BIGINT NOT NULL,
  to_account    BIGINT NOT NULL,
  owner_name    VARCHAR(100) NOT NULL,
  amount        DECIMAL(15,2) NOT NULL,
  note          TEXT,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (from_account) REFERENCES accounts(id),
  FOREIGN KEY (to_account)   REFERENCES accounts(id)
);

INSERT IGNORE INTO users VALUES
  (1, 'Alice Admin',  'alice@vulnbank.com',  'hashed_pw_1', 'ROLE_ADMIN', NOW()),
  (2, 'Bob User',     'bob@vulnbank.com',    'hashed_pw_2', 'ROLE_USER',  NOW()),
  (3, 'Carol User',   'carol@vulnbank.com',  'hashed_pw_3', 'ROLE_USER',  NOW());

INSERT IGNORE INTO accounts VALUES
  (1, 1, 'Alice Admin', 'VB-0001-0001', 99999.00, NOW()),
  (2, 2, 'Bob User',    'VB-0002-0001', 5000.00,  NOW()),
  (3, 3, 'Carol User',  'VB-0003-0001', 3200.00,  NOW());

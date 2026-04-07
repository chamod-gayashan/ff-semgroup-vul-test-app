-- =============================================================
-- Seed: Accounts
-- Each active user receives one primary account.
-- Account number format: SG-<owner_id_padded>-<seq>
-- =============================================================

USE semgroup_app_db;

INSERT INTO accounts (id, owner_id, owner_name, account_no, balance) VALUES
  (1, 1, 'Alice Admin', 'SG-0001-001', 250000.00),
  (2, 2, 'Bob User',    'SG-0002-001',  15000.00),
  (3, 3, 'Carol User',  'SG-0003-001',   8750.50),
  (4, 4, 'Dave User',   'SG-0004-001',   3200.00),
  -- Bob's second (savings) account
  (5, 2, 'Bob User',    'SG-0002-002',  42000.00)
ON DUPLICATE KEY UPDATE
  owner_name = VALUES(owner_name),
  balance    = VALUES(balance);

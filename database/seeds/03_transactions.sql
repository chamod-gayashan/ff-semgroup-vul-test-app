-- =============================================================
-- Seed: Transactions
-- Covers transfers between the seeded accounts.
-- from_account / to_account reference accounts.id
-- =============================================================

USE semgroup_app_db;

INSERT INTO transactions (id, from_account, to_account, owner_name, amount, note, created_at) VALUES
  -- Alice transfers to Bob
  (1,  1, 2, 'Alice Admin', 5000.00, 'Monthly allowance',           '2025-01-05 09:00:00'),
  -- Bob transfers to Carol
  (2,  2, 3, 'Bob User',    1200.00, 'Rent payment',                '2025-01-10 14:30:00'),
  -- Carol transfers to Dave
  (3,  3, 4, 'Carol User',   500.00, 'Dinner split',                '2025-01-15 19:45:00'),
  -- Dave transfers to Bob
  (4,  4, 2, 'Dave User',    300.00, 'Book purchase reimbursement', '2025-01-20 11:00:00'),
  -- Bob moves money between his own accounts
  (5,  2, 5, 'Bob User',   10000.00, 'Savings transfer',            '2025-02-01 08:00:00'),
  -- Alice transfers to Carol
  (6,  1, 3, 'Alice Admin', 2500.00, 'Project bonus',               '2025-02-14 16:00:00'),
  -- Bob transfers to Alice
  (7,  2, 1, 'Bob User',     750.00, 'Loan repayment',              '2025-03-01 10:00:00'),
  -- Carol transfers to Alice
  (8,  3, 1, 'Carol User',   200.00, 'Shared subscription',         '2025-03-10 12:00:00'),
  -- Dave transfers to Carol
  (9,  4, 3, 'Dave User',    150.00, 'Utilities share',             '2025-03-20 17:30:00'),
  -- Alice large transfer to Bob savings
  (10, 1, 5, 'Alice Admin', 20000.00, 'Investment contribution',    '2025-04-01 09:00:00')
ON DUPLICATE KEY UPDATE
  amount     = VALUES(amount),
  note       = VALUES(note),
  created_at = VALUES(created_at);

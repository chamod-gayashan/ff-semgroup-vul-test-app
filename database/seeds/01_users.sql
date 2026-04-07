-- =============================================================
-- Seed: Users
-- WARNING: Passwords are intentionally stored as unsalted MD5
-- hashes to mirror the sample application's (deliberately
-- insecure) hashing logic.  Do NOT use MD5 in production.
--
--   alice@semgroup.com  →  admin123    →  0192023a7bbd73250516f069df18b500
--   bob@semgroup.com    →  password123 →  482c811da5d5b4bc6d497ffa98491e38
--   carol@semgroup.com  →  user123     →  6ad14ba9986e3615423dfca256d04e3f
--   dave@semgroup.com   →  test123     →  cc03e747a6afbbcbf8be7668acfebee5
--   eve@semgroup.com    →  secret123   →  5d7845ac6ee7cfffafc5fe5f35cf666d
-- =============================================================

USE semgroup_app_db;

INSERT INTO users (id, full_name, email, password, role, is_active) VALUES
  (1, 'Alice Admin',   'alice@semgroup.com', '0192023a7bbd73250516f069df18b500', 'ROLE_ADMIN', 1),
  (2, 'Bob User',      'bob@semgroup.com',   '482c811da5d5b4bc6d497ffa98491e38', 'ROLE_USER',  1),
  (3, 'Carol User',    'carol@semgroup.com', '6ad14ba9986e3615423dfca256d04e3f', 'ROLE_USER',  1),
  (4, 'Dave User',     'dave@semgroup.com',  'cc03e747a6afbbcbf8be7668acfebee5', 'ROLE_USER',  1),
  (5, 'Eve Inactive',  'eve@semgroup.com',   '5d7845ac6ee7cfffafc5fe5f35cf666d', 'ROLE_USER',  0)
ON DUPLICATE KEY UPDATE
  full_name  = VALUES(full_name),
  password   = VALUES(password),
  role       = VALUES(role),
  is_active  = VALUES(is_active);

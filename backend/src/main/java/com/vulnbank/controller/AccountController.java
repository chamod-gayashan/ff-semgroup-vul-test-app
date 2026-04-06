package com.vulnbank.controller;

import com.vulnbank.model.Account;
import com.vulnbank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final DataSource dataSource;

    public AccountController(AccountRepository accountRepository, DataSource dataSource) {
        this.accountRepository = accountRepository;
        this.dataSource = dataSource;
    }

    // VULN-ID: VB-001 | CWE-89 | Severity: CRITICAL
    // SQL Injection: raw string concatenation used to build JDBC query.
    // Attacker can send: name=' OR '1'='1 to dump all accounts.
    // SAFE VERSION (commented out):
    //   PreparedStatement ps = conn.prepareStatement(
    //       "SELECT * FROM accounts WHERE owner_name = ?");
    //   ps.setString(1, name);
    //   ResultSet rs = ps.executeQuery();
    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam("name") String name) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM accounts WHERE owner_name = '" + name + "'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<Map<String, Object>> results = new ArrayList<>();
            while (rs.next()) {
                results.add(Map.of(
                        "id", rs.getLong("id"),
                        "ownerName", rs.getString("owner_name"),
                        "accountNo", rs.getString("account_no"),
                        "balance", rs.getBigDecimal("balance")
                ));
            }
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // VULN-ID: VB-002 | CWE-639 | Severity: HIGH
    // Broken Object Level Auth / IDOR: account is fetched by ID with no ownership
    // check against the authenticated user, so any user can read any account balance.
    // SAFE VERSION (commented out):
    //   if (!account.getOwnerId().equals(currentUser.getId())) {
    //       throw new AccessDeniedException("Forbidden");
    //   }
    @GetMapping("/{id}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long id) {
        try {
            Account account = accountRepository.findById(id).orElseThrow();
            BigDecimal balance = account.getBalance();
            return ResponseEntity.ok(Map.of("id", id, "balance", balance));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "Account not found"));
        }
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

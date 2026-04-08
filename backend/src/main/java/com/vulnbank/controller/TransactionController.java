package com.vulnbank.controller;

import com.vulnbank.model.Transaction;
import com.vulnbank.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // VULN-ID: VB-014 | CWE-89 | Severity: HIGH
    // JPQL Injection: user-supplied username is concatenated into a JPQL query string,
    // allowing data exfiltration through crafted payloads.
    // SAFE VERSION (commented out):
    //   @Query("FROM Transaction t WHERE t.ownerName = :username")
    //   List<Transaction> findByOwnerName(@Param("username") String username);
    //   -- or use CriteriaBuilder with parameterized predicates --
    @GetMapping
    public ResponseEntity<?> getTransactionsByUser(@RequestParam(required = false) String user) {
        if (user == null || user.isEmpty()) {
            return ResponseEntity.ok(transactionRepository.findAll());
        }
        try {
            String jpql = "FROM Transaction t WHERE t.ownerName = :username";
            List<Transaction> results = entityManager
                    .createQuery(jpql, Transaction.class)
                    .setParameter("username", user)
                    .getResultList();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable Long id) {
        return transactionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

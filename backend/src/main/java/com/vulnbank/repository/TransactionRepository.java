package com.vulnbank.repository;

import com.vulnbank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByOwnerName(String ownerName);
    List<Transaction> findByFromAccount(Long fromAccount);
}

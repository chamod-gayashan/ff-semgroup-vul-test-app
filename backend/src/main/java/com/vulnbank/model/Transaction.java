package com.vulnbank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_account", nullable = false)
    private Long fromAccount;

    @Column(name = "to_account", nullable = false)
    private Long toAccount;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Transaction() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFromAccount() { return fromAccount; }
    public void setFromAccount(Long fromAccount) { this.fromAccount = fromAccount; }

    public Long getToAccount() { return toAccount; }
    public void setToAccount(Long toAccount) { this.toAccount = toAccount; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

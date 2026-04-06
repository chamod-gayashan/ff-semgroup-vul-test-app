package com.vulnbank.service;

import com.vulnbank.model.Account;
import com.vulnbank.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> findByOwnerId(Long ownerId) {
        return accountRepository.findByOwnerId(ownerId);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }
}

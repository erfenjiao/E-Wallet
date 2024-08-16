package com.example.ewallet.domains.account.repository;

import com.example.ewallet.domains.account.model.Account.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findByUserId(Long userId);
    void save(Account account);
}

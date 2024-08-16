package com.example.ewallet.infrastructure.persistence.Impl;

import com.example.ewallet.domains.account.model.Account.Account;
import com.example.ewallet.domains.account.repository.AccountRepository;
import com.example.ewallet.infrastructure.persistence.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private AccountJpaRepository accountJpaRepository;
    @Override
    public Optional<Account> findByUserId(Long userId) {
        return accountJpaRepository.findByUserId(userId);
    }

    @Override
    public void save(Account account) {
        accountJpaRepository.save(account);
    }
}

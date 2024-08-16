package com.example.ewallet.domains.transaction.repository;

import com.example.ewallet.domains.transaction.model.transaction.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    public Transaction save(Transaction transaction);
    Optional<Transaction> findById(Long transactionId);

    Optional<Transaction> findByFromUserId(Long userId);
}

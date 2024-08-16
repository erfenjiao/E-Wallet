package com.example.ewallet.infrastructure.persistence.Impl;

import com.example.ewallet.domains.transaction.model.transaction.Transaction;
import com.example.ewallet.domains.transaction.repository.TransactionRepository;
import com.example.ewallet.infrastructure.persistence.TransactionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    TransactionJpaRepository transactionJpaRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionJpaRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> findByFromUserId(Long userId) {
        return transactionJpaRepository.findByFromUserId(userId);
    }

    @Override
    public Optional<Transaction> findById(Long transactionId) {
        return transactionJpaRepository.findById(transactionId);
    }
}

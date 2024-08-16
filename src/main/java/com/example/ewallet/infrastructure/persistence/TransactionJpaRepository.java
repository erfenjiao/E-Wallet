package com.example.ewallet.infrastructure.persistence;

import com.example.ewallet.domains.transaction.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByFromUserId(Long fromUserId);


}

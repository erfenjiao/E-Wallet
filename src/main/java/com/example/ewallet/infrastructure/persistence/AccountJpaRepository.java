package com.example.ewallet.infrastructure.persistence;

import com.example.ewallet.domains.account.model.Account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long userId);

    Optional<Account> findByUserId(Long userId);
}

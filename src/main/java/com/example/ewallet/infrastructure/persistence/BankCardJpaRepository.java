package com.example.ewallet.infrastructure.persistence;

import com.example.ewallet.domains.account.model.banckcard.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankCardJpaRepository extends JpaRepository<BankCard, Long> {
    List<BankCard> findByAccountId(Long accountId);

    Optional<BankCard> findByAccountIdAndIsDefault(Long accountId, boolean isDefault);
}

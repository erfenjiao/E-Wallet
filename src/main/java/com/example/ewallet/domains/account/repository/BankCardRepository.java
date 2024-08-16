package com.example.ewallet.domains.account.repository;

import com.example.ewallet.domains.account.model.banckcard.BankCard;

import java.util.List;
import java.util.Optional;

public interface BankCardRepository {
    Optional<BankCard> findByCardNumber(Long cardNumber);

    void save(BankCard bankCard);

    List<BankCard> findByAccountId(Long accountId);

    Optional<BankCard> findByAccountIdAndIsDefault(Long accountId, boolean isDefault);

}

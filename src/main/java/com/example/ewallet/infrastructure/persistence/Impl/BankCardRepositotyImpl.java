package com.example.ewallet.infrastructure.persistence.Impl;

import com.example.ewallet.domains.account.model.banckcard.BankCard;
import com.example.ewallet.domains.account.repository.BankCardRepository;
import com.example.ewallet.infrastructure.persistence.BankCardJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BankCardRepositotyImpl implements BankCardRepository {
    @Autowired
    private BankCardJpaRepository bankCardJpaRepository;

    public Optional<BankCard> findByCardNumber(Long cardNumber) {
        return bankCardJpaRepository.findById(cardNumber);
    }


    @Override
    public void save(BankCard bankCard) {
        bankCardJpaRepository.save(bankCard);
    }

    @Override
    public List<BankCard> findByAccountId(Long accountId) {
        return bankCardJpaRepository.findByAccountId(accountId);
    }

    @Override
    public Optional<BankCard> findByAccountIdAndIsDefault(Long accountId, boolean isDefault) {
        return bankCardJpaRepository.findByAccountIdAndIsDefault(accountId, isDefault);
    }


}

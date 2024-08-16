package com.example.ewallet.application.service;

import com.example.ewallet.domains.transaction.model.transaction.Transaction;
import com.example.ewallet.domains.transaction.model.transaction.TransactionStatus;
import com.example.ewallet.domains.transaction.model.transaction.TransactionType;
import com.example.ewallet.domains.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 创建账单
     * @param fromUserId
     * @param toUserId
     * @param transactionType
     * @param amount
     */
    @Transactional
    public void createTransaction( Long fromUserId, Long toUserId, TransactionType transactionType, BigDecimal amount) {
        Transaction transaction = new Transaction(fromUserId, toUserId, transactionType, amount);
        transactionRepository.save(transaction);
    }

    //查询所有订单
    public Optional<Transaction> getTransactionByUserId(Long UserId) {
        return transactionRepository.findByFromUserId(UserId);
    }

    public void updateTransactionStatus(Long transactionId, TransactionStatus newStatus) {
        //transactionRepository.updateTransactionStatus(transactionId, newStatus);
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        transaction.setTransactionStatus(newStatus);
        transactionRepository.save(transaction);
    }

    /**
     *
     @Transactional
     public void updateTransactionStatus(Long transactionId, TransactionStatus newStatus) {
     Transaction transaction = transactionRepository.findById(transactionId)
     .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

     transaction.setTransactionStatus(newStatus);
     transaction.setTimestamp(new Date());
     transactionRepository.save(transaction);
     }
     *
     *     @Transactional
     *     public void deposit(Long userId, Money amount) {
     *         Transaction transaction = createTransaction(null, userId, TransactionType.DEPOSIT, amount);
     *
     *         try {
     *             balanceService.deposit(userId, amount);
     *             updateTransactionStatus(transaction.getId(), TransactionStatus.COMPLETED);
     *         } catch (Exception e) {
     *             updateTransactionStatus(transaction.getId(), TransactionStatus.FAILED);
     *             throw e;
     *         }
     *     }
     *
     *     @Transactional
     *     public void withdraw(Long userId, Money amount) {
     *         Transaction transaction = createTransaction(userId, null, TransactionType.WITHDRAWAL, amount);
     *
     *         try {
     *             balanceService.withdraw(userId, amount);
     *             updateTransactionStatus(transaction.getId(), TransactionStatus.COMPLETED);
     *         } catch (Exception e) {
     *             updateTransactionStatus(transaction.getId(), TransactionStatus.FAILED);
     *             throw e;
     *         }
     *     }
     *
     *     @Transactional
     *     public void payment(Long fromUserId, Long toUserId, Money amount) {
     *         Transaction transaction = createTransaction(fromUserId, toUserId, TransactionType.PAYMENT, amount);
     *
     *         try {
     *             balanceService.withdraw(fromUserId, amount);
     *             balanceService.deposit(toUserId, amount);
     *             updateTransactionStatus(transaction.getId(), TransactionStatus.COMPLETED);
     *         } catch (Exception e) {
     *             updateTransactionStatus(transaction.getId(), TransactionStatus.FAILED);
     *             throw e;
     *         }
     *     }
     */
}

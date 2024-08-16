package com.example.ewallet.application.service;

import com.example.ewallet.application.dto.BankCardDto;
import com.example.ewallet.domains.account.model.Account.Account;
import com.example.ewallet.domains.account.model.Account.AccountStatus;
import com.example.ewallet.domains.account.model.banckcard.BankCard;
import com.example.ewallet.domains.account.repository.AccountRepository;
import com.example.ewallet.domains.account.repository.BankCardRepository;
import com.example.ewallet.domains.transaction.model.transaction.TransactionStatus;
import com.example.ewallet.domains.transaction.model.transaction.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankCardRepository bankCardRepository;

    @Autowired
    private TransactionService transactionService;

    /**
     * 查询余额
     * TODO：查询账户余额只涉及到了一个 accountRepository，可以放在 Accountdomain 的 service 里面
     * @param userId
     * @return
     */
    public BigDecimal getBalance(Long userId) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (!accountOpt.isPresent()) {
            log.error("Account not found for user id: " + userId);
        }
        System.out.println("accountOpt = "+accountOpt);
        Account account = accountOpt.get();
        System.out.println("account"+account);
        BigDecimal balance = account.getBalance();
        return balance;
    }

    /**
     * 绑定银行卡
     * public class BankCardDto {
     *     private Long cardNumber;
     *     private String cardPassword;
     * }
     * @param userId
     * @param bankCardDto
     */
    public void bindBankCard(Long userId, BankCardDto bankCardDto) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (!accountOpt.isPresent()) {
            log.error("Account not found for user id: " + userId);
        }
        Account account = accountOpt.get();
        Optional<BankCard> bankCardOpt = bankCardRepository.findByCardNumber(bankCardDto.getCardNumber());
        if (!bankCardOpt.isPresent()) {
            log.error("BankCard not found for BnakCard Number: " + bankCardDto.getCardNumber());
        }
        // 比对银行卡密码是否一致
        if (!bankCardOpt.get().getPassword().matches(bankCardDto.getCardPassword())) {
            log.error("BankCard password not match");
        }
        BankCard bankCard = bankCardOpt.get();
        bankCard.setAccountId(account.getId());

        // 如果账户还没有默认卡，将这张卡设为默认卡
        if (!bankCardRepository.findByAccountIdAndIsDefault(account.getId(), true).isPresent()) {
            bankCard.setDefault(true);
        }

        bankCardRepository.save(bankCard);
    }

    /**
     * 设置默认卡
     * @param userId
     * @param cardNumber
     */
    public void setDefaultBankCard(Long userId, Long cardNumber) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();

            // 找到当前默认卡并取消默认状态
            bankCardRepository.findByAccountIdAndIsDefault(account.getId(), true).ifPresent(card -> {
                card.setDefault(false);
                bankCardRepository.save(card);
            });

            // 设置新的默认卡
            bankCardRepository.findByCardNumber(cardNumber).ifPresent(card -> {
                card.setDefault(true);
                bankCardRepository.save(card);
            });
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }


    /**
     * 解绑银行卡
     * @param userId
     * @param cardNumber
     */
    public void unbindBankCard(Long userId, Long cardNumber) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (accountOpt.isPresent()) {
            Optional<BankCard> bankCardOpt = bankCardRepository.findByCardNumber(cardNumber);
            if (bankCardOpt.isPresent()) {
                BankCard bankCard = bankCardOpt.get();
                if (bankCard.getAccountId().equals(accountOpt.get().getId())) {
                    bankCard.setAccountId(null);
                    bankCard.setDefault(false); // 解绑时取消默认状态
                    bankCardRepository.save(bankCard);
                } else {
                    throw new IllegalArgumentException("Bank card not bound to this account");
                }
            } else {
                throw new IllegalArgumentException("Bank card not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    /**
     * 获取账户已经绑定了的银行卡
     * @param userId
     * @return
     */
    public List<BankCard> getBankCards(Long userId) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if(!accountOpt.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        //
        // TODO ： 只应该获取银行卡的卡号，不能获取密码
        //
        return bankCardRepository.findByAccountId(accountOpt.get().getId());
    }

    /**
     * 银行卡向账户转账
     * @param userId
     * @param cardNumber
     * @param amount
     */
    public void transferBankCardToBalance(Long userId, Long cardNumber, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            Optional<BankCard> bankCardOpt = bankCardRepository.findByCardNumber(cardNumber);
            if (bankCardOpt.isPresent()) {
                BankCard bankCard = bankCardOpt.get();
                if (bankCard.getAccountId().equals(account.getId())) {
                    if (bankCard.getBalance().compareTo(amount) >= 0) {
                        bankCard.setBalance(bankCard.getBalance().subtract(amount));
                        account.setBalance(account.getBalance().add(amount));
                        account.setUpdate_time(new Date());
                        // 生成 transaction 账单
                        transactionService.createTransaction(bankCard.getAccountId(), account.getId(), TransactionType.DEPOSIT, amount);
                        // 保存
                        bankCardRepository.save(bankCard);
                        accountRepository.save(account);
                        transactionService.updateTransactionStatus(transactionService.getTransactionByUserId(userId).get().getId(), TransactionStatus.COMPLETED);
                    } else {
                        transactionService.updateTransactionStatus(transactionService.getTransactionByUserId(userId).get().getId(), TransactionStatus.FAILED);
                        throw new IllegalArgumentException("Insufficient funds on bank card");
                    }
                } else {
                    throw new IllegalArgumentException("Bank card not bound to this account");
                }
            } else {
                throw new IllegalArgumentException("Bank card not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    /**
     * 提现
     * @param userId
     * @param cardNumber
     * @param amount
     */
    public void withdrawBankCardToBalance(Long userId, Long cardNumber, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (accountOpt.isPresent()){
            Account account = accountOpt.get();
            Optional<BankCard> bankCardOpt = bankCardRepository.findByCardNumber(cardNumber);
            if (bankCardOpt.isPresent()) {
                BankCard bankCard = bankCardOpt.get();
                // 账户扣款
                if (bankCard.getAccountId().equals(account.getId())) {
                    // 判断 account 余额是否足够
                    if (account.getBalance().compareTo(amount) > 0) {
                        account.setBalance(account.getBalance().subtract(amount));
                        bankCard.setBalance(bankCard.getBalance().add(amount));
                        account.setUpdate_time(new Date());
                        transactionService.createTransaction(account.getId(), bankCard.getAccountId(), TransactionType.WITHDRAWAL, amount);
                        accountRepository.save(account);
                        bankCardRepository.save(bankCard);
                        transactionService.updateTransactionStatus(transactionService.getTransactionByUserId(userId).get().getId(), TransactionStatus.COMPLETED);
                    } else {
                        transactionService.updateTransactionStatus(transactionService.getTransactionByUserId(userId).get().getId(), TransactionStatus.FAILED);
                        throw new IllegalArgumentException("Insufficient funds on account");
                    }
                } else {
                    throw new IllegalArgumentException("account not bound to this Bank card");
                }
            } else {
                throw new IllegalArgumentException("Bank card not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }


    /**
     * 冻结账户的一部分钱
     * @param userId
     * @param amount
     */
    public void freezeBalance(Long userId, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (accountOpt.isPresent()){
            Account account = accountOpt.get();
            if(account.getBalance().compareTo(amount) > 0) {
                account.setBalance(account.getBalance().subtract(amount));
                account.setFrozenBalance(account.getFrozenBalance().add(amount));
                account.setUpdate_time(new Date());
                accountRepository.save(account);
            } else {
                throw new IllegalArgumentException("Insufficient funds on account");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }


    /**
     * 解冻
     * @param userId
     * @param amount
     */
    public void unfreezeBalance(Long userId, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (accountOpt.isPresent()){
            Account account = accountOpt.get();
            // 冻结的钱数 > 解冻的钱数
            if(account.getFrozenBalance().compareTo(amount) > 0) {
                account.setFrozenBalance(account.getFrozenBalance().subtract(amount));
                account.setBalance(account.getBalance().add(amount));
                account.setUpdate_time(new Date());
                accountRepository.save(account);
            } else {
                throw new IllegalArgumentException("冻结的钱数 < 解冻的钱数");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Transactional
    public void updateAccountStatus(Long userId, AccountStatus newStatus) {
        Account accountBalance = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        accountBalance.setAccountStatus(newStatus);
        accountBalance.setUpdate_time(new java.util.Date());
        accountRepository.save(accountBalance);
    }

    public AccountStatus getAccountStatus(Long userId) {
        Account accountBalance = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return accountBalance.getAccountStatus();
    }

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        Optional<Account> fromAccountOpt = accountRepository.findByUserId(fromUserId);
        if (fromAccountOpt.isPresent()){
            Account fromAccount = fromAccountOpt.get();
            Optional<Account> toAccountOpt = accountRepository.findByUserId(toUserId);
            if (toAccountOpt.isPresent()) {
                Account toAccount = toAccountOpt.get();
                if (fromAccount.getBalance().compareTo(amount) > 0) {
                    fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                    toAccount.setBalance(toAccount.getBalance().add(amount));
                    fromAccount.setUpdate_time(new Date());
                    toAccount.setUpdate_time(new Date());
                    transactionService.createTransaction(fromAccount.getId(), toAccount.getId(), TransactionType.TRANSFER, amount);
                    accountRepository.save(fromAccount);
                    transactionService.updateTransactionStatus(transactionService.getTransactionByUserId(fromUserId).get().getId(), TransactionStatus.COMPLETED);
                } else {
                    throw new IllegalArgumentException("Insufficient funds on account");
                }
            } else {
                throw new IllegalArgumentException("toUserId not found");
            }
        } else {
            throw new IllegalArgumentException("fromUser not found");
        }

    }

    public Account getAccountInfo(Long userId) {
        return accountRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }


    /**
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

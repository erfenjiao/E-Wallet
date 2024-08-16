package com.example.ewallet.interfaces.controller;

import com.example.ewallet.application.dto.AccountDto;
import com.example.ewallet.application.dto.BankCardDto;
import com.example.ewallet.application.dto.CardAccountTransferDto;
import com.example.ewallet.application.dto.TransactionDto;
import com.example.ewallet.application.service.AccountService;
import com.example.ewallet.domains.account.model.Account.Account;
import com.example.ewallet.domains.account.model.Account.AccountStatus;
import com.example.ewallet.domains.account.model.banckcard.BankCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/api/v1/accounts")
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    /**
     * 获取用户余额
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long userId) {
        BigDecimal balanceOpt = accountService.getBalance(userId);
        return ResponseEntity.ok(balanceOpt);
    }

    /**
     * 绑定银行卡
     * @param userId
     * @param bankCardDto
     * @return
     */
    @PostMapping("/{userId}/bind-card")
    public ResponseEntity<Void> bindBankCard(@PathVariable Long userId, @RequestBody BankCardDto bankCardDto) {
        accountService.bindBankCard(userId, bankCardDto);
        return ResponseEntity.ok().build();
    }


    /**
     * 设置默认银行卡
     * @param userId
     * @param bankCardDto
     * @return
     */
    @PostMapping("/{userId}/set-default-card")
    public ResponseEntity<Void> setDefaultBankCard(@PathVariable Long userId, @RequestBody BankCardDto bankCardDto) {
        accountService.setDefaultBankCard(userId, bankCardDto.getCardNumber());
        return ResponseEntity.ok().build();
    }

    /**
     * 解绑银行卡
     * @param userId
     * @param bankCardDto
     * @return
     */
    @PostMapping("/{userId}/unbind-card")
    public ResponseEntity<Void> unbindBankCard(@PathVariable Long userId, @RequestBody BankCardDto bankCardDto) {
        accountService.unbindBankCard(userId, bankCardDto.getCardNumber());
        return ResponseEntity.ok().build();
    }

    /**
     * 获取银行卡列表
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/bank-cards")
    public ResponseEntity<List<BankCard>> getBankCards(@PathVariable Long userId) {
        List<BankCard> bankCards = accountService.getBankCards(userId);
        return ResponseEntity.ok(bankCards);
    }

    /**
     * 银行卡向账户转账
     * @param userId
     * @param transferDto
     * @return
     */
    @PostMapping("/{userId}/transferBankCardToBalance")
    public ResponseEntity<Void> transferBankCardToBalance(@PathVariable Long userId, @RequestBody CardAccountTransferDto transferDto) {
        accountService.transferBankCardToBalance(userId, transferDto.getCardNumber(), transferDto.getAmount());
        return ResponseEntity.ok().build();
    }

    /**
     * 账户向银行卡转钱 - 提现
     * @param userId
     * @param transferDto
     * @return
     */
    @PostMapping("/{userId}/withdrawBankCardToBalance")
    public ResponseEntity<String> withdrawBankCardToBalance(@PathVariable Long userId, @RequestBody CardAccountTransferDto transferDto) {
        accountService.withdrawBankCardToBalance(userId, transferDto.getCardNumber(), transferDto.getAmount());
        return ResponseEntity.ok("账户向银行卡转钱 - 提现成功");
    }

    /**
     * 冻结余额
     * @param userId
     * @param amount
     * @return
     */
    @PostMapping("/{userId}/freeze")
    public ResponseEntity<Void> freezeBalance(@PathVariable Long userId, @RequestBody BigDecimal amount) {
        accountService.freezeBalance(userId, amount);
        return ResponseEntity.ok().build();
    }

    /**
     * 解冻余额
     * @param userId
     * @param amount
     * @return
     */
    @PostMapping("/{userId}/unfreeze")
    public ResponseEntity<Void> unfreezeBalance(@PathVariable Long userId, @RequestBody BigDecimal amount) {
        accountService.unfreezeBalance(userId, amount);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新账户状态
     * @param userId
     * @param accountDto
     * @return
     */
    @PostMapping("/{userId}/status")
    public ResponseEntity<Void> updateAccountStatus(@PathVariable Long userId, @RequestBody AccountDto accountDto) {
        accountService.updateAccountStatus(userId, accountDto.getAccountStatus());
        return ResponseEntity.ok().build();
    }

    /**
     * 获取账户状态
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/status")
    public ResponseEntity<AccountStatus> getAccountStatus(@PathVariable Long userId) {
        AccountStatus accountStatus = accountService.getAccountStatus(userId);
        return ResponseEntity.ok(accountStatus);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransactionDto transactionDto) {
        accountService.transfer(transactionDto.getFromUserId(), transactionDto.getToUserId(), transactionDto.getAmount());
        return ResponseEntity.ok("转账成功");
    }

    @GetMapping("{userId}")
    public ResponseEntity<Account> getAccountInfo(@PathVariable Long userId) {
        Account account = accountService.getAccountInfo(userId);
        return ResponseEntity.ok(account);
    }

}

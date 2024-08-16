package com.example.ewallet.domains.account.model.banckcard;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class BankCard {
    @Id
    private Long cardNumber;

    private String password;

    @Column(name = "account_id")
    private Long accountId;

    // 银行卡金额
    private BigDecimal balance = BigDecimal.ZERO;

    private boolean isDefault; // 表示是否为默认卡
}

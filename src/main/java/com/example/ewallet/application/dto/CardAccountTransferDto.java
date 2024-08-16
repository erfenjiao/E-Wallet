package com.example.ewallet.application.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 银行卡向账户余额转账
 */
@Data
public class CardAccountTransferDto {
    private Long cardNumber;
    private BigDecimal amount;
}

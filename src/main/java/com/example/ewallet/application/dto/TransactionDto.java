package com.example.ewallet.application.dto;

import com.example.ewallet.domains.transaction.model.transaction.TransactionStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {

    private Long fromUserId;

    private Long toUserId;

    private BigDecimal amount;

    private TransactionStatus transactionStatus;
}

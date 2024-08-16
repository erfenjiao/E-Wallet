package com.example.ewallet.application.dto;

import com.example.ewallet.domains.account.model.Account.AccountStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private Long user_id;
    private BigDecimal balance;
    private AccountStatus accountStatus;

}

package com.example.ewallet.application.dto;

import com.example.ewallet.domains.transaction.model.transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatusDto {
    private TransactionStatus status;
}

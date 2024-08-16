package com.example.ewallet.interfaces.controller;

import com.example.ewallet.application.service.TransactionService;
import com.example.ewallet.domains.transaction.model.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/transactions")
@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("{userId}")
    public ResponseEntity<Transaction> getTransactionByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getTransactionByUserId(userId).get());
    }

}

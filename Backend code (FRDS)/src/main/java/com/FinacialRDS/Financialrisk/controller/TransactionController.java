package com.FinacialRDS.Financialrisk.controller;

import com.FinacialRDS.Financialrisk.entity.Transaction;
import com.FinacialRDS.Financialrisk.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.processTransaction(transaction));
    }
}
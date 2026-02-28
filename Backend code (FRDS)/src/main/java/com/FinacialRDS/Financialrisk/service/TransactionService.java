package com.FinacialRDS.Financialrisk.service;

import com.FinacialRDS.Financialrisk.entity.Alert;
import com.FinacialRDS.Financialrisk.entity.Transaction;
import com.FinacialRDS.Financialrisk.entity.User;
import com.FinacialRDS.Financialrisk.repository.AlertRepository;
import com.FinacialRDS.Financialrisk.repository.TransactionRepository;
import com.FinacialRDS.Financialrisk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AlertRepository alertRepository;

    @Transactional
    public Transaction processTransaction(Transaction transaction) {
        // Fetch the actual user from database to avoid detached entity issue
        if (transaction.getUser() != null && transaction.getUser().getUserId() != null) {
            User user = userRepository.findById(transaction.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + transaction.getUser().getUserId()));
            transaction.setUser(user);
        }
        
        // Save the transaction first
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Risk Detection Logic: Flag high-value transactions
        System.out.println("Transaction amount: " + savedTransaction.getAmount());
        if (savedTransaction.getAmount() != null && savedTransaction.getAmount() > 10000) {
            System.out.println("Creating alert for high-value transaction!");
            Alert alert = Alert.builder()
                    .transaction(savedTransaction)
                    .alertType("HIGH_AMOUNT")
                    .status("UNREAD")
                    .build();
            Alert savedAlert = alertRepository.save(alert);
            System.out.println("Alert saved with ID: " + savedAlert.getAlertId());
        } else {
            System.out.println("Amount is not greater than 10000, no alert created.");
        }
        
        return savedTransaction;
    }
}

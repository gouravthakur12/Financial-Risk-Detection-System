package com.FinacialRDS.Financialrisk.service;

import com.FinacialRDS.Financialrisk.entity.Alert;
import com.FinacialRDS.Financialrisk.entity.Transaction;
import com.FinacialRDS.Financialrisk.entity.User;
import com.FinacialRDS.Financialrisk.repository.AlertRepository;
import com.FinacialRDS.Financialrisk.repository.TransactionRepository;
import com.FinacialRDS.Financialrisk.repository.UserRepository;
import com.FinacialRDS.Financialrisk.integration.FraudDetectionClient;
import com.FinacialRDS.Financialrisk.risk.RiskEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AlertRepository alertRepository;
    private final RiskEngine riskEngine;
    private final FraudDetectionClient fraudDetectionClient;

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
        
        // Risk Detection Logic: use RiskEngine for decision & severity
        // obtain fraud probability from ML integration
        Double amount = savedTransaction.getAmount();
        Double fraudProb = null;
        if (savedTransaction.getUser() != null && savedTransaction.getUser().getUserId() != null) {
            fraudProb = fraudDetectionClient.getFraudProbability(
                    amount, savedTransaction.getUser().getUserId());
        }
        savedTransaction.setFraudProbability(fraudProb);

        System.out.println("Transaction amount: " + amount + ", fraudProb: " + fraudProb);

        if (riskEngine.shouldGenerateAlert(amount, fraudProb)) {
            System.out.println("Generating alert via RiskEngine");
            String severity = riskEngine.calculateSeverity(amount, fraudProb);
            Alert alert = Alert.builder()
                    .transaction(savedTransaction)
                    .alertType("HIGH_VALUE_TRANSACTION")
                    .severity(severity)
                    .status("PENDING_REVIEW")
                    .build();
            Alert savedAlert = alertRepository.save(alert);
            System.out.println("Alert saved with ID: " + savedAlert.getAlertId());
        } else {
            System.out.println("RiskEngine decided no alert needed.");
        }
        
        return savedTransaction;
    }
}

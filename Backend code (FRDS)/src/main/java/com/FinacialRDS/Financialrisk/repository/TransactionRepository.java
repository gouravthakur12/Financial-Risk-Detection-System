package com.FinacialRDS.Financialrisk.repository;

import com.FinacialRDS.Financialrisk.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserUserId(Long userId); // Find all transactions for a specific user
}
package com.FinacialRDS.Financialrisk.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne // Many transactions belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount")
    private Double amount;
    
    @Column(name = "category")
    private String category;
    
    @CreationTimestamp
    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Column(name = "fraud_probability")
    private Double fraudProbability;
    
    @Column(name = "anomaly_score")
    private Double anomalyScore;
    
    @Column(name = "risk_score")
    private Double riskScore;

    @Column(name = "status")
    private String status; // e.g., PENDING, COMPLETED, FLAG_FRAUD
}
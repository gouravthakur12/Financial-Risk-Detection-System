package com.FinacialRDS.Financialrisk.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(name = "alert_type")
    private String alertType;

    @Column(name = "status")
    private String status; // UNREAD, READ, RESOLVED

    @Column(name = "severity")
    private String severity;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

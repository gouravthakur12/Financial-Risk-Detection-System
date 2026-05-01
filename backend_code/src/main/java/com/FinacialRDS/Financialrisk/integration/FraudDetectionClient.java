package com.FinacialRDS.Financialrisk.integration;

public interface FraudDetectionClient {

    Double getFraudProbability(Double amount, Long userId);
}
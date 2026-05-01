package com.FinacialRDS.Financialrisk.integration;

import org.springframework.stereotype.Component;

@Component
public class MockFraudDetectionClient implements FraudDetectionClient {

    @Override
    public Double getFraudProbability(Double amount, Long userId) {
        return Math.random(); // temporary simulation
    }
}
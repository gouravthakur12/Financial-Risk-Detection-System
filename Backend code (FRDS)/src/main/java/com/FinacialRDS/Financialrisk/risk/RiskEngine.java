package com.FinacialRDS.Financialrisk.risk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RiskEngine {

    @Value("${risk.threshold.medium}")
    private Double mediumThreshold;

    @Value("${risk.threshold.high}")
    private Double highThreshold;

    public String calculateSeverity(Double amount, Double fraudProbability) {

        if (fraudProbability != null && fraudProbability >= 0.9) {
            return "HIGH";
        }

        if (amount == null) {
            return "LOW";
        }
        if (amount >= highThreshold) {
            return "HIGH";
        } else if (amount >= mediumThreshold) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    public boolean shouldGenerateAlert(Double amount, Double fraudProbability) {
        if (fraudProbability != null && fraudProbability >= 0.8) {
            return true;
        }
        return amount != null && amount >= mediumThreshold;
    }
}

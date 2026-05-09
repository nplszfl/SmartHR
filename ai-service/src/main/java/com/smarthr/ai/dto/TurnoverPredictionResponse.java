package com.smarthr.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnoverPredictionResponse {
    private String employeeId;
    private double riskScore; // 0-100
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    private List<String> riskFactors;
    private List<String> protectiveFactors;
    private Map<String, Double> factorContributions;
    private List<RetentionSuggestion> retentionSuggestions;
    private int predictedTenureMonths;
    private double confidence;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RetentionSuggestion {
        private String action;
        private String category; // COMPENSATION, WORK_LIFE, GROWTH, MANAGEMENT
        private int priority; // 1-5
        private String expectedImpact;
        private String timeline;
    }
}

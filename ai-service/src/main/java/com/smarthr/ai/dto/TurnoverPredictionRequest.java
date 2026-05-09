package com.smarthr.ai.dto;

import jakarta.validation.constraints.NotBlank;
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
public class TurnoverPredictionRequest {
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
    
    private String department;
    private int tenureMonths;
    private int satisfactionScore; // 1-10
    private int performanceScore; // 1-5
    private double salaryRatio; // salary/market average
    private int recentPromotion; // 0 or 1
    private int workLifeBalance; // 1-10
    private String managerId;
    private int teamSize;
    private double commuteHours;
    private List<String> recentAchievements;
    private List<String> warningSigns;
    private Map<String, Object> additionalFactors;
}

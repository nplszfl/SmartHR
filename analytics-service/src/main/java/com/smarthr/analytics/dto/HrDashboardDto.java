package com.smarthr.analytics.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class HrDashboardDto {
    // Headcount metrics
    private Long totalEmployees;
    private Long activeEmployees;
    private Long onLeaveEmployees;
    private Long terminatedEmployees;
    private BigDecimal headcountGrowthRate;
    
    // Department breakdown
    private Map<String, Long> employeesByDepartment;
    private Map<String, Long> headcountByDepartment;
    
    // Hiring metrics
    private Long totalCandidates;
    private Long candidatesInPipeline;
    private Long hiredCandidates;
    private Long rejectedCandidates;
    private BigDecimal hiringConversionRate;
    
    // Job metrics
    private Long openPositions;
    private Long totalApplications;
    private BigDecimal applicationPerOpening;
    
    // Interview metrics
    private Long totalInterviews;
    private Long completedInterviews;
    private Long upcomingInterviews;
    private BigDecimal interviewNoShowRate;
    
    // Time metrics
    private Double averageTimeToHire;
    private Double averageInterviewCountPerCandidate;
    
    // Performance metrics
    private BigDecimal averagePerformanceRating;
    private Long employeesDueForReview;
    
    // Diversity metrics
    private Map<String, Object> diversityMetrics;
    
    // Turnover metrics
    private BigDecimal turnoverRate;
    private BigDecimal voluntaryTurnoverRate;
    private BigDecimal involuntaryTurnoverRate;
    
    // Timestamp
    private LocalDateTime generatedAt;
}
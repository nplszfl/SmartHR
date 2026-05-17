package com.smarthr.analytics.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class HiringReportDto {
    private Long totalCandidates;
    private Long totalHired;
    private Long totalRejected;
    private Long totalInPipeline;
    
    // Conversion rates
    private BigDecimal applicationToInterviewRate;
    private BigDecimal interviewToOfferRate;
    private BigDecimal offerToAcceptRate;
    private BigDecimal overallConversionRate;
    
    // Time metrics
    private Double averageTimeToHire;
    private Double averageTimeToInterview;
    private Double averageTimeToOffer;
    private Double averageTimeToAccept;
    
    // Source effectiveness
    private Map<String, Long> candidatesBySource;
    private Map<String, BigDecimal> hiredBySource;
    private Map<String, BigDecimal> conversionRateBySource;
    
    // Department hiring
    private Map<String, Long> positionsOpenedByDepartment;
    private Map<String, Long> hiresByDepartment;
    private Map<String, Long> openPositionsByDepartment;
    
    // Quality of hire
    private BigDecimal averageHiringManagerRating;
    private BigDecimal newHireRetentionRate;
    
    // Trends
    private List<HiringTrendPoint> hiringTrend;
    
    // Period
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime generatedAt;
    
    @Data
    public static class HiringTrendPoint {
        private LocalDate date;
        private Long applications;
        private Long interviews;
        private Long offers;
        private Long hires;
    }
}
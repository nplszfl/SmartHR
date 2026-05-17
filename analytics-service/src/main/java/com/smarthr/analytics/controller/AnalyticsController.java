package com.smarthr.analytics.controller;

import com.smarthr.analytics.dto.HiringReportDto;
import com.smarthr.analytics.dto.HrDashboardDto;
import com.smarthr.analytics.service.AnalyticsService;
import com.smarthr.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ApiResponse<HrDashboardDto> getDashboard() {
        return ApiResponse.success(analyticsService.getDashboard());
    }

    @GetMapping("/hiring-report")
    public ApiResponse<HiringReportDto> getHiringReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(analyticsService.getHiringReport(startDate, endDate));
    }

    @GetMapping("/department-headcount")
    public ApiResponse<Map<String, Long>> getDepartmentHeadcount() {
        return ApiResponse.success(analyticsService.getDepartmentHeadcount());
    }

    @GetMapping("/hiring-funnel")
    public ApiResponse<Map<String, Object>> getHiringFunnel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(analyticsService.getHiringFunnel(startDate, endDate));
    }

    @GetMapping("/time-to-hire")
    public ApiResponse<Map<String, Double>> getTimeToHireMetrics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(analyticsService.getTimeToHireMetrics(startDate, endDate));
    }

    @GetMapping("/source-effectiveness")
    public ApiResponse<Map<String, Object>> getSourceEffectiveness(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(analyticsService.getSourceEffectiveness(startDate, endDate));
    }

    @GetMapping("/diversity")
    public ApiResponse<Map<String, Object>> getDiversityMetrics() {
        return ApiResponse.success(analyticsService.getDiversityMetrics());
    }

    @GetMapping("/turnover")
    public ApiResponse<Map<String, Object>> getTurnoverAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(analyticsService.getTurnoverAnalytics(startDate, endDate));
    }

    @GetMapping("/compensation")
    public ApiResponse<Map<String, Object>> getCompensationAnalytics() {
        return ApiResponse.success(analyticsService.getCompensationAnalytics());
    }

    @GetMapping("/interviews")
    public ApiResponse<Map<String, Object>> getInterviewAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(analyticsService.getInterviewAnalytics(startDate, endDate));
    }

    @GetMapping("/pipeline-health")
    public ApiResponse<Map<String, Object>> getPipelineHealth() {
        return ApiResponse.success(analyticsService.getPipelineHealth());
    }

    @GetMapping("/offers")
    public ApiResponse<Map<String, Object>> getOfferMetrics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(analyticsService.getOfferMetrics(startDate, endDate));
    }

    @GetMapping("/growth")
    public ApiResponse<Map<String, BigDecimal>> getGrowthMetrics(
            @RequestParam String metric,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentEnd,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate previousStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate previousEnd) {
        return ApiResponse.success(analyticsService.getGrowthMetrics(metric, currentStart, currentEnd, previousStart, previousEnd));
    }

    @GetMapping("/recruiter-performance")
    public ApiResponse<Map<String, Object>> getRecruiterPerformance(
            @RequestParam Long recruiterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(analyticsService.getRecruiterPerformance(recruiterId, startDate, endDate));
    }
}
package com.smarthr.ai.controller;

import com.smarthr.ai.client.DeepSeekClient;
import com.smarthr.ai.dto.*;
import com.smarthr.ai.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final ResumeParsingService resumeParsingService;
    private final ResumeScoringService resumeScoringService;
    private final InterviewAnalysisService interviewAnalysisService;
    private final InterviewQuestionsService interviewQuestionsService;
    private final TurnoverPredictionService turnoverPredictionService;
    private final TalentMatchingService talentMatchingService;
    private final DeepSeekClient deepSeekClient;

    @Value("${deepseek.api.model}")
    private String deepSeekModel;

    /**
     * POST /api/v1/ai/resume/parse
     * AI extracts and structures resume information
     */
    @PostMapping("/resume/parse")
    public ResponseEntity<ApiResponse<ResumeParseResponse>> parseResume(
            @Valid @RequestBody ResumeParseRequest request) {
        log.info("Received resume parse request for candidate: {}", request.getCandidateName());
        
        try {
            ResumeParseResponse response = resumeParsingService.parseResume(request);
            return ResponseEntity.ok(ApiResponse.success("Resume parsed successfully", response));
        } catch (Exception e) {
            log.error("Failed to parse resume: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to parse resume: " + e.getMessage()));
        }
    }

    /**
     * POST /api/v1/ai/resume/score
     * AI scores resume against job requirements
     */
    @PostMapping("/resume/score")
    public ResponseEntity<ApiResponse<ResumeScoreResponse>> scoreResume(
            @Valid @RequestBody ResumeScoreRequest request) {
        log.info("Received resume score request for job: {}", request.getJobTitle());
        
        try {
            ResumeScoreResponse response = resumeScoringService.scoreResume(request);
            return ResponseEntity.ok(ApiResponse.success("Resume scored successfully", response));
        } catch (Exception e) {
            log.error("Failed to score resume: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to score resume: " + e.getMessage()));
        }
    }

    /**
     * POST /api/v1/ai/interview/analyze
     * AI analyzes interview performance and generates feedback
     */
    @PostMapping("/interview/analyze")
    public ResponseEntity<ApiResponse<InterviewAnalysisResponse>> analyzeInterview(
            @Valid @RequestBody InterviewAnalysisRequest request) {
        log.info("Received interview analysis request for candidate: {}", request.getCandidateName());
        
        try {
            InterviewAnalysisResponse response = interviewAnalysisService.analyzeInterview(request);
            return ResponseEntity.ok(ApiResponse.success("Interview analyzed successfully", response));
        } catch (Exception e) {
            log.error("Failed to analyze interview: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to analyze interview: " + e.getMessage()));
        }
    }

    /**
     * POST /api/v1/ai/interview/questions
     * AI generates interview questions based on job description
     */
    @PostMapping("/interview/questions")
    public ResponseEntity<ApiResponse<InterviewQuestionsResponse>> generateInterviewQuestions(
            @Valid @RequestBody InterviewQuestionsRequest request) {
        log.info("Received interview questions request for type: {}", request.getInterviewType());
        
        try {
            InterviewQuestionsResponse response = interviewQuestionsService.generateQuestions(request);
            return ResponseEntity.ok(ApiResponse.success("Interview questions generated successfully", response));
        } catch (Exception e) {
            log.error("Failed to generate interview questions: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to generate interview questions: " + e.getMessage()));
        }
    }

    /**
     * POST /api/v1/ai/turnover/predict
     * AI predicts employee turnover risk
     */
    @PostMapping("/turnover/predict")
    public ResponseEntity<ApiResponse<TurnoverPredictionResponse>> predictTurnover(
            @Valid @RequestBody TurnoverPredictionRequest request) {
        log.info("Received turnover prediction request for employee: {}", request.getEmployeeId());
        
        try {
            TurnoverPredictionResponse response = turnoverPredictionService.predictTurnover(request);
            return ResponseEntity.ok(ApiResponse.success("Turnover risk predicted successfully", response));
        } catch (Exception e) {
            log.error("Failed to predict turnover: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to predict turnover: " + e.getMessage()));
        }
    }

    /**
     * POST /api/v1/ai/talent/match
     * AI matches candidates to best-fit jobs
     */
    @PostMapping("/talent/match")
    public ResponseEntity<ApiResponse<TalentMatchResponse>> matchTalent(
            @Valid @RequestBody TalentMatchRequest request) {
        log.info("Received talent match request for {} candidates and {} jobs",
                request.getCandidates().size(), request.getJobs().size());
        
        try {
            TalentMatchResponse response = talentMatchingService.matchTalent(request);
            return ResponseEntity.ok(ApiResponse.success("Talent matching completed successfully", response));
        } catch (Exception e) {
            log.error("Failed to match talent: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to match talent: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/ai/health
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<HealthResponse>> healthCheck() {
        boolean deepSeekConnected = false;
        try {
            deepSeekConnected = deepSeekClient.healthCheck();
        } catch (Exception e) {
            log.warn("DeepSeek health check failed: {}", e.getMessage());
        }

        HealthResponse health = HealthResponse.builder()
                .status(deepSeekConnected ? "UP" : "DEGRADED")
                .service("SmartHR AI Service")
                .version("1.0.0")
                .timestamp(LocalDateTime.now())
                .deepseekConnected(deepSeekConnected)
                .deepseekModel(deepSeekConnected ? deepSeekModel : null)
                .build();

        if (deepSeekConnected) {
            return ResponseEntity.ok(ApiResponse.success("Service is healthy", health));
        } else {
            return ResponseEntity.status(503)
                    .body(ApiResponse.error("DeepSeek API is not connected", health));
        }
    }

    /**
     * Global exception handler for validation errors
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Validation failed: " + errors));
    }

    /**
     * Global exception handler for other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericError(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }
}

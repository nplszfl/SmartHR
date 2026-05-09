package com.smarthr.job.controller;

import com.smarthr.job.ai.JobAnalysisRequest;
import com.smarthr.job.ai.JobAnalysisResponse;
import com.smarthr.job.ai.AIJobService;
import com.smarthr.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/jobs")
@RequiredArgsConstructor
public class AIJobController {
    private final AIJobService aiJobService;

    @PostMapping("/analyze")
    public ApiResponse<JobAnalysisResponse> analyzeJob(@RequestBody JobAnalysisRequest request) {
        return ApiResponse.success(aiJobService.analyzeJob(request));
    }

    @PostMapping("/generate-description")
    public ApiResponse<String> generateDescription(@RequestBody Map<String, String> request) {
        String description = aiJobService.generateJobDescription(
            request.get("title"),
            request.get("requirements")
        );
        return ApiResponse.success(description);
    }

    @PostMapping("/suggest-questions")
    public ApiResponse<List<String>> suggestQuestions(@RequestBody Map<String, String> request) {
        List<String> questions = aiJobService.suggestInterviewQuestions(
            request.get("jobTitle"),
            request.get("requirements")
        );
        return ApiResponse.success(questions);
    }

    @PostMapping("/calculate-match")
    public ApiResponse<Double> calculateMatch(@RequestBody Map<String, String> request) {
        Double score = aiJobService.calculateMatchScore(
            request.get("candidateSkills"),
            request.get("jobRequirements")
        );
        return ApiResponse.success(score);
    }
}
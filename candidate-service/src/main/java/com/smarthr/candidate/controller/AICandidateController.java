package com.smarthr.candidate.controller;

import com.smarthr.candidate.ai.ResumeAnalysisRequest;
import com.smarthr.candidate.ai.ResumeAnalysisResponse;
import com.smarthr.candidate.ai.AICandidateService;
import com.smarthr.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/candidates")
@RequiredArgsConstructor
public class AICandidateController {
    private final AICandidateService aiCandidateService;

    @PostMapping("/analyze-resume")
    public ApiResponse<ResumeAnalysisResponse> analyzeResume(@RequestBody ResumeAnalysisRequest request) {
        return ApiResponse.success(aiCandidateService.analyzeResume(request));
    }

    @PostMapping("/generate-summary")
    public ApiResponse<String> generateSummary(@RequestBody Map<String, String> request) {
        String summary = aiCandidateService.generateCandidateSummary(
            request.get("candidateName"),
            request.get("resumeText")
        );
        return ApiResponse.success(summary);
    }

    @PostMapping("/suggest-questions")
    public ApiResponse<List<String>> suggestQuestions(@RequestBody Map<String, String> request) {
        List<String> questions = aiCandidateService.suggestInterviewQuestions(
            request.get("jobRequirements"),
            request.get("candidateSkills")
        );
        return ApiResponse.success(questions);
    }
}
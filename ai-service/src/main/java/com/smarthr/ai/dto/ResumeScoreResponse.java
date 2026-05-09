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
public class ResumeScoreResponse {
    private String candidateId;
    private String resumeId;
    private String jobId;
    private double overallScore;
    private SkillScore skillScore;
    private ExperienceScore experienceScore;
    private EducationScore educationScore;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> missingRequirements;
    private Map<String, Double> keywordMatchScores;
    private String recommendation;
    private String analysis;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillScore {
        private double score;
        private List<String> matched;
        private List<String> missing;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceScore {
        private double score;
        private int yearsMatched;
        private String relevance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationScore {
        private double score;
        private String matchedDegree;
        private boolean meetsRequirement;
    }
}

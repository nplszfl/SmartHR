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
public class InterviewAnalysisResponse {
    private String candidateName;
    private String position;
    private double overallScore;
    private TechnicalAssessment technicalAssessment;
    private CommunicationAssessment communicationAssessment;
    private BehavioralAssessment behavioralAssessment;
    private List<String> strengths;
    private List<String> concerns;
    private List<String> redFlags;
    private List<QuestionFeedback> questionFeedback;
    private String overallFeedback;
    private String hiringRecommendation;
    private double confidence;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechnicalAssessment {
        private double score;
        private List<String> strongAreas;
        private List<String> weakAreas;
        private List<String> technicalSkillsDemonstrated;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunicationAssessment {
        private double score;
        private String clarity;
        private String articulation;
        private List<String> communicationStrengths;
        private List<String> communicationWeaknesses;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BehavioralAssessment {
        private double score;
        private Map<String, String> competencies;
        private List<String> culturalFitIndicators;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionFeedback {
        private String question;
        private String answer;
        private double score;
        private String feedback;
    }
}

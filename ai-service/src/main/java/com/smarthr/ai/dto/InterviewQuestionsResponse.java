package com.smarthr.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionsResponse {
    private String jobTitle;
    private String interviewType;
    private List<Question> questions;
    private List<String> evaluationCriteria;
    private int estimatedDurationMinutes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        private int number;
        private String category;
        private String question;
        private String type; // OPEN_ENDED, SITUATIONAL, TECHNICAL
        private String expectedAnswer;
        private List<String> goodAnswerIndicators;
        private List<String> poorAnswerIndicators;
        private int maxScore;
    }
}

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
public class TalentMatchResponse {
    private List<CandidateJobMatch> matches;
    private int totalCandidates;
    private int totalJobs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CandidateJobMatch {
        private String candidateId;
        private String candidateName;
        private List<JobMatch> jobMatches;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobMatch {
        private String jobId;
        private String jobTitle;
        private double matchScore;
        private String matchLevel; // PERFECT, GOOD, MODERATE, WEAK
        private List<String> matchedSkills;
        private List<String> missingSkills;
        private List<String> bonusSkills;
        private Map<String, Double> skillScores;
        private String recommendation;
    }
}

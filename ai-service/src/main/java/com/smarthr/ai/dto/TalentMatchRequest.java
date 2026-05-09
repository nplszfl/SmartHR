package com.smarthr.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalentMatchRequest {
    @NotEmpty(message = "At least one candidate profile is required")
    private List<CandidateProfile> candidates;
    
    @NotEmpty(message = "At least one job is required")
    private List<JobProfile> jobs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CandidateProfile {
        private String candidateId;
        private String name;
        private String currentTitle;
        private List<String> skills;
        private List<String> preferredRoles;
        private int yearsExperience;
        private String educationLevel;
        private String location;
        private double salaryExpectation;
        private List<String> industries;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobProfile {
        private String jobId;
        private String title;
        private String department;
        private List<String> requiredSkills;
        private List<String> preferredSkills;
        private int minYearsExperience;
        private String educationRequirement;
        private String location;
        private double salaryRange;
        private List<String> industries;
    }
}

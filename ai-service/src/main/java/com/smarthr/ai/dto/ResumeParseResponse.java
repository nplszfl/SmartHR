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
public class ResumeParseResponse {
    private String candidateId;
    private String name;
    private String email;
    private String phone;
    private String location;
    private String summary;
    private List<String> skills;
    private List<WorkExperience> workExperience;
    private List<Education> education;
    private List<String> certifications;
    private Map<String, Object> additionalInfo;
    private double confidence;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkExperience {
        private String company;
        private String title;
        private String startDate;
        private String endDate;
        private String description;
        private List<String> achievements;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Education {
        private String institution;
        private String degree;
        private String field;
        private String graduationYear;
    }
}

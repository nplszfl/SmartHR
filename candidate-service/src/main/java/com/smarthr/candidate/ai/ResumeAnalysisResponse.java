package com.smarthr.candidate.ai;

import lombok.Data;
import java.util.List;

@Data
public class ResumeAnalysisResponse {
    private Double matchScore;
    private List<String> skillsMatched;
    private List<String> skillsMissing;
    private String summary;
    private List<String> recommendations;
    private Integer suggestedExperienceLevel;
}
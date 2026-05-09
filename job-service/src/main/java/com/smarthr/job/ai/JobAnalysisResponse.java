package com.smarthr.job.ai;

import lombok.Data;
import java.util.List;

@Data
public class JobAnalysisResponse {
    private List<String> suggestedSkills;
    private String marketSalaryRange;
    private String candidatePersona;
    private List<String> interviewFocusAreas;
    private Double difficultyScore;
    private String hiringTimeline;
}
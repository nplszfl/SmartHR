package com.smarthr.job.ai;

import lombok.Data;
import java.util.List;

@Data
public class JobAnalysisRequest {
    private String jobTitle;
    private String requirements;
    private String description;
    private List<String> requiredSkills;
}
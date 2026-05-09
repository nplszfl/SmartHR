package com.smarthr.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeScoreRequest {
    @NotBlank(message = "Resume text is required")
    private String resumeText;
    
    @NotBlank(message = "Job requirements are required")
    private String jobTitle;
    
    @NotBlank(message = "Job description is required")
    private String jobDescription;
    
    private List<String> requiredSkills;
    private List<String> preferredSkills;
    private int maxScore = 100;
}

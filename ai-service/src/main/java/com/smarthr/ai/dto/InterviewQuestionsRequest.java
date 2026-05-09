package com.smarthr.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionsRequest {
    @NotBlank(message = "Job description is required")
    private String jobDescription;
    
    @NotBlank(message = "Interview type is required")
    private String interviewType; // TECHNICAL, BEHAVIORAL, MIXED
    
    private String candidateBackground;
    private int numberOfQuestions;
    private String focusArea; // Optional focus on specific skills
}

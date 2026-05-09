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
public class InterviewAnalysisRequest {
    @NotBlank(message = "Interview transcript is required")
    private String transcript;
    
    private String candidateName;
    private String position;
    private String interviewType; // TECHNICAL, BEHAVIORAL, HR, PANEL
    private int interviewDurationMinutes;
    private String interviewerNotes;
}

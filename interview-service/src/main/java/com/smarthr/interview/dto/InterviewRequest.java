package com.smarthr.interview.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class InterviewRequest {
    @NotNull(message = "Candidate ID is required")
    private Long candidateId;
    
    @NotNull(message = "Job ID is required")
    private Long jobId;
    
    @NotNull(message = "Interviewer ID is required")
    private Long interviewerId;
    
    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledAt;
    
    private Integer duration = 60;
    private String location;
    private String meetingLink;
    private Integer type = 1;
    private String notes;
}
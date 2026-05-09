package com.smarthr.candidate.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CandidateRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    private String resumeUrl;
    private Integer status = 1;
    private Long jobId;
    private String education;
    private Integer experienceYears;
    private String skills;
    private String source;
    private String notes;
    private Long interviewerId;
}
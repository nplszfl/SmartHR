package com.smarthr.candidate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CandidateDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String resumeUrl;
    private Integer status;
    private Long jobId;
    private String jobTitle;
    private String education;
    private Integer experienceYears;
    private String skills;
    private String source;
    private String notes;
    private LocalDateTime appliedAt;
    private Long interviewerId;
    private String interviewerName;
    private LocalDateTime createTime;
}
package com.smarthr.interview.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InterviewDTO {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private Long jobId;
    private String jobTitle;
    private Long interviewerId;
    private String interviewerName;
    private LocalDateTime scheduledAt;
    private Integer duration;
    private String location;
    private String meetingLink;
    private Integer type;
    private Integer status;
    private String notes;
    private Integer rating;
    private String feedback;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String resultRecommendation;
    private LocalDateTime createTime;
}
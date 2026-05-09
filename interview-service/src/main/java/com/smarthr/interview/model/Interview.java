package com.smarthr.interview.model;

import com.smarthr.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("interview")
public class Interview extends BaseEntity {
    private Long candidateId;
    private Long jobId;
    private Long interviewerId;
    private LocalDateTime scheduledAt;
    private Integer duration; // in minutes
    private String location;
    private String meetingLink;
    private Integer type; // 1: phone, 2: video, 3: onsite
    private Integer status; // 1: scheduled, 2: in-progress, 3: completed, 4: cancelled, 5: rescheduled
    private String notes;
    private Integer rating; // 1-5
    private String feedback;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String resultRecommendation; // strong_hire, hire, no_hire, strong_no_hire
}
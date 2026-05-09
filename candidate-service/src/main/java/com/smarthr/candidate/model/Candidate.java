package com.smarthr.candidate.model;

import com.smarthr.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("candidate")
public class Candidate extends BaseEntity {
    private String name;
    private String email;
    private String phone;
    private String resumeUrl;
    private Integer status; // 1: new, 2: screening, 3: interview, 4: offered, 5: hired, 6: rejected
    private Long jobId;
    private String education;
    private Integer experienceYears;
    private String skills;
    private String source; // website, referral, linkedin, etc.
    private String notes;
    private LocalDateTime appliedAt;
    private Long interviewerId;
}
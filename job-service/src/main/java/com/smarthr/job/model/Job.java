package com.smarthr.job.model;

import com.smarthr.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("job")
public class Job extends BaseEntity {
    private String title;
    private String department;
    private String location;
    private String employmentType; // full-time, part-time, contract
    private String description;
    private String requirements;
    private String responsibilities;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency;
    private Integer status; // 1: draft, 2: open, 3: paused, 4: closed
    private Long postedBy;
    private LocalDateTime publishedAt;
    private LocalDateTime closedAt;
    private Integer applicationCount;
    private String benefits;
    private String remoteOption;
}
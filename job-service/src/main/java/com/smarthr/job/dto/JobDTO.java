package com.smarthr.job.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobDTO {
    private Long id;
    private String title;
    private String department;
    private String location;
    private String employmentType;
    private String description;
    private String requirements;
    private String responsibilities;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency;
    private Integer status;
    private Long postedBy;
    private String postedByName;
    private LocalDateTime publishedAt;
    private LocalDateTime closedAt;
    private Integer applicationCount;
    private String benefits;
    private String remoteOption;
    private LocalDateTime createTime;
}
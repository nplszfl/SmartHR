package com.smarthr.job.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class JobRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    private String location;
    private String employmentType = "full-time";
    private String description;
    private String requirements;
    private String responsibilities;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency = "USD";
    private Integer status = 1;
    private Long postedBy;
    private String benefits;
    private String remoteOption;
}
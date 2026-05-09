package com.smarthr.candidate.ai;

import lombok.Data;
import java.util.List;

@Data
public class ResumeAnalysisRequest {
    private String candidateName;
    private String resumeText;
    private String jobRequirements;
}
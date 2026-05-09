package com.smarthr.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeParseRequest {
    @NotBlank(message = "Resume text is required")
    private String resumeText;
    
    private String candidateName;
    private String email;
}

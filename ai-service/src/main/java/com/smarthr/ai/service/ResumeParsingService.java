package com.smarthr.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthr.ai.client.DeepSeekClient;
import com.smarthr.ai.dto.ResumeParseRequest;
import com.smarthr.ai.dto.ResumeParseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeParsingService {

    private final DeepSeekClient deepSeekClient;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
        You are an expert HR resume parser. Your task is to extract and structure information from resumes.
        Extract the following information and return ONLY valid JSON:
        - name: Full name of the candidate
        - email: Email address
        - phone: Phone number
        - location: City/Location
        - summary: Professional summary (2-3 sentences)
        - skills: Array of technical and soft skills
        - workExperience: Array of work experiences with company, title, startDate, endDate, description, achievements
        - education: Array of education entries with institution, degree, field, graduationYear
        - certifications: Array of certifications
        - additionalInfo: Any other relevant information as an object
        
        Return null for any field that cannot be determined. Be precise and extract exact information when present.
        """;

    public ResumeParseResponse parseResume(ResumeParseRequest request) {
        log.info("Parsing resume for candidate: {}", request.getCandidateName() != null ? 
                request.getCandidateName() : "Unknown");

        String userPrompt = buildParsingPrompt(request);

        String response = deepSeekClient.chat(SYSTEM_PROMPT, userPrompt);

        try {
            return parseResponse(response, request);
        } catch (Exception e) {
            log.error("Failed to parse resume response: {}", e.getMessage());
            return createFallbackResponse(request);
        }
    }

    private String buildParsingPrompt(ResumeParseRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Please parse the following resume and extract all information in JSON format:\n\n");
        prompt.append(request.getResumeText());
        
        if (request.getCandidateName() != null) {
            prompt.append("\n\nCandidate name (if visible): ").append(request.getCandidateName());
        }
        if (request.getEmail() != null) {
            prompt.append("\nCandidate email (if visible): ").append(request.getEmail());
        }
        
        prompt.append("\n\nReturn ONLY the JSON object, no additional text.");
        
        return prompt.toString();
    }

    private ResumeParseResponse parseResponse(String response, ResumeParseRequest request) throws JsonProcessingException {
        String cleanedResponse = cleanJsonResponse(response);
        JsonNode root = objectMapper.readTree(cleanedResponse);

        ResumeParseResponse.ResumeParseResponseBuilder builder = ResumeParseResponse.builder()
                .candidateId(UUID.randomUUID().toString())
                .name(getTextValue(root, "name"))
                .email(getTextValue(root, "email"))
                .phone(getTextValue(root, "phone"))
                .location(getTextValue(root, "location"))
                .summary(getTextValue(root, "summary"))
                .confidence(0.85);

        // Parse skills
        if (root.has("skills") && root.get("skills").isArray()) {
            List<String> skills = new ArrayList<>();
            root.get("skills").forEach(node -> skills.add(node.asText()));
            builder.skills(skills);
        }

        // Parse work experience
        if (root.has("workExperience") && root.get("workExperience").isArray()) {
            List<ResumeParseResponse.WorkExperience> experiences = new ArrayList<>();
            root.get("workExperience").forEach(node -> {
                ResumeParseResponse.WorkExperience exp = ResumeParseResponse.WorkExperience.builder()
                        .company(getTextValue(node, "company"))
                        .title(getTextValue(node, "title"))
                        .startDate(getTextValue(node, "startDate"))
                        .endDate(getTextValue(node, "endDate"))
                        .description(getTextValue(node, "description"))
                        .build();
                
                if (node.has("achievements") && node.get("achievements").isArray()) {
                    List<String> achievements = new ArrayList<>();
                    node.get("achievements").forEach(a -> achievements.add(a.asText()));
                    exp.setAchievements(achievements);
                }
                experiences.add(exp);
            });
            builder.workExperience(experiences);
        }

        // Parse education
        if (root.has("education") && root.get("education").isArray()) {
            List<ResumeParseResponse.Education> educationList = new ArrayList<>();
            root.get("education").forEach(node -> {
                ResumeParseResponse.Education edu = ResumeParseResponse.Education.builder()
                        .institution(getTextValue(node, "institution"))
                        .degree(getTextValue(node, "degree"))
                        .field(getTextValue(node, "field"))
                        .graduationYear(getTextValue(node, "graduationYear"))
                        .build();
                educationList.add(edu);
            });
            builder.education(educationList);
        }

        // Parse certifications
        if (root.has("certifications") && root.get("certifications").isArray()) {
            List<String> certifications = new ArrayList<>();
            root.get("certifications").forEach(node -> certifications.add(node.asText()));
            builder.certifications(certifications);
        }

        // Parse additional info
        if (root.has("additionalInfo") && root.get("additionalInfo").isObject()) {
            builder.additionalInfo(Map.of("raw", root.get("additionalInfo").toString()));
        }

        return builder.build();
    }

    private String cleanJsonResponse(String response) {
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }

    private String getTextValue(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : null;
    }

    private ResumeParseResponse createFallbackResponse(ResumeParseRequest request) {
        return ResumeParseResponse.builder()
                .candidateId(UUID.randomUUID().toString())
                .name(request.getCandidateName())
                .email(request.getEmail())
                .confidence(0.0)
                .skills(new ArrayList<>())
                .workExperience(new ArrayList<>())
                .education(new ArrayList<>())
                .certifications(new ArrayList<>())
                .build();
    }
}

package com.smarthr.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthr.ai.client.DeepSeekClient;
import com.smarthr.ai.dto.ResumeScoreRequest;
import com.smarthr.ai.dto.ResumeScoreResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeScoringService {

    private final DeepSeekClient deepSeekClient;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
        You are an expert HR recruitment analyst. Your task is to evaluate resumes against job requirements.
        Analyze the resume and job description, then provide a comprehensive scoring report in JSON format.
        
        Return a JSON object with the following structure:
        - overallScore: Overall match score from 0-100
        - skillScore: Object with score (0-100), matched array, missing array
        - experienceScore: Object with score (0-100), yearsMatched, relevance
        - educationScore: Object with score (0-100), matchedDegree, meetsRequirement (boolean)
        - strengths: Array of candidate strengths
        - weaknesses: Array of candidate weaknesses
        - missingRequirements: Array of missing job requirements
        - keywordMatchScores: Object mapping keywords to match scores (0-1)
        - recommendation: Brief hiring recommendation
        - analysis: Detailed analysis explanation
        
        Be objective and evidence-based in your evaluation.
        """;

    public ResumeScoreResponse scoreResume(ResumeScoreRequest request) {
        log.info("Scoring resume for job: {}", request.getJobTitle());

        String userPrompt = buildScoringPrompt(request);

        String response = deepSeekClient.chat(SYSTEM_PROMPT, userPrompt);

        try {
            return parseResponse(response, request);
        } catch (Exception e) {
            log.error("Failed to parse scoring response: {}", e.getMessage());
            return createFallbackResponse(request);
        }
    }

    private String buildScoringPrompt(ResumeScoreRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Evaluate this resume against the job requirements.\n\n");
        prompt.append("RESUME:\n").append(request.getResumeText());
        prompt.append("\n\nJOB TITLE: ").append(request.getJobTitle());
        prompt.append("\n\nJOB DESCRIPTION:\n").append(request.getJobDescription());
        
        if (request.getRequiredSkills() != null && !request.getRequiredSkills().isEmpty()) {
            prompt.append("\n\nREQUIRED SKILLS: ");
            prompt.append(String.join(", ", request.getRequiredSkills()));
        }
        if (request.getPreferredSkills() != null && !request.getPreferredSkills().isEmpty()) {
            prompt.append("\n\nPREFERRED SKILLS: ");
            prompt.append(String.join(", ", request.getPreferredSkills()));
        }
        
        prompt.append("\n\nReturn ONLY the JSON object, no additional text.");
        
        return prompt.toString();
    }

    private ResumeScoreResponse parseResponse(String response, ResumeScoreRequest request) throws JsonProcessingException {
        String cleanedResponse = cleanJsonResponse(response);
        JsonNode root = objectMapper.readTree(cleanedResponse);

        ResumeScoreResponse.ResumeScoreResponseBuilder builder = ResumeScoreResponse.builder()
                .overallScore(getDoubleValue(root, "overallScore", 50.0))
                .recommendation(getTextValue(root, "recommendation"))
                .analysis(getTextValue(root, "analysis"));

        // Parse skill score
        if (root.has("skillScore") && root.get("skillScore").isObject()) {
            JsonNode skillNode = root.get("skillScore");
            builder.skillScore(ResumeScoreResponse.SkillScore.builder()
                    .score(getDoubleValue(skillNode, "score", 50.0))
                    .matched(parseStringArray(skillNode, "matched"))
                    .missing(parseStringArray(skillNode, "missing"))
                    .build());
        }

        // Parse experience score
        if (root.has("experienceScore") && root.get("experienceScore").isObject()) {
            JsonNode expNode = root.get("experienceScore");
            builder.experienceScore(ResumeScoreResponse.ExperienceScore.builder()
                    .score(getDoubleValue(expNode, "score", 50.0))
                    .yearsMatched(expNode.has("yearsMatched") ? expNode.get("yearsMatched").asInt() : 0)
                    .relevance(getTextValue(expNode, "relevance"))
                    .build());
        }

        // Parse education score
        if (root.has("educationScore") && root.get("educationScore").isObject()) {
            JsonNode eduNode = root.get("educationScore");
            builder.educationScore(ResumeScoreResponse.EducationScore.builder()
                    .score(getDoubleValue(eduNode, "score", 50.0))
                    .matchedDegree(getTextValue(eduNode, "matchedDegree"))
                    .meetsRequirement(eduNode.has("meetsRequirement") && eduNode.get("meetsRequirement").asBoolean())
                    .build());
        }

        builder.strengths(parseStringArray(root, "strengths"));
        builder.weaknesses(parseStringArray(root, "weaknesses"));
        builder.missingRequirements(parseStringArray(root, "missingRequirements"));

        // Parse keyword match scores
        if (root.has("keywordMatchScores") && root.get("keywordMatchScores").isObject()) {
            Map<String, Double> keywordScores = new HashMap<>();
            root.get("keywordMatchScores").fields().forEachRemaining(field -> 
                keywordScores.put(field.getKey(), field.getValue().asDouble()));
            builder.keywordMatchScores(keywordScores);
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

    private double getDoubleValue(JsonNode node, String field, double defaultValue) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asDouble() : defaultValue;
    }

    private List<String> parseStringArray(JsonNode node, String field) {
        List<String> result = new ArrayList<>();
        if (node.has(field) && node.get(field).isArray()) {
            node.get(field).forEach(n -> result.add(n.asText()));
        }
        return result;
    }

    private ResumeScoreResponse createFallbackResponse(ResumeScoreRequest request) {
        return ResumeScoreResponse.builder()
                .overallScore(50.0)
                .skillScore(ResumeScoreResponse.SkillScore.builder()
                        .score(50.0)
                        .matched(new ArrayList<>())
                        .missing(request.getRequiredSkills() != null ? request.getRequiredSkills() : new ArrayList<>())
                        .build())
                .experienceScore(ResumeScoreResponse.ExperienceScore.builder()
                        .score(50.0)
                        .yearsMatched(0)
                        .relevance("Unable to analyze")
                        .build())
                .educationScore(ResumeScoreResponse.EducationScore.builder()
                        .score(50.0)
                        .meetsRequirement(false)
                        .build())
                .strengths(new ArrayList<>())
                .weaknesses(new ArrayList<>())
                .missingRequirements(new ArrayList<>())
                .recommendation("Unable to generate recommendation")
                .analysis("Service temporarily unavailable")
                .build();
    }
}

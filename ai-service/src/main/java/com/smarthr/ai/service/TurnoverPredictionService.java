package com.smarthr.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthr.ai.client.DeepSeekClient;
import com.smarthr.ai.dto.TurnoverPredictionRequest;
import com.smarthr.ai.dto.TurnoverPredictionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TurnoverPredictionService {

    private final DeepSeekClient deepSeekClient;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
        You are an expert HR analytics specialist. Your task is to predict employee turnover risk 
        and provide actionable retention insights.
        
        Analyze the employee data and return a JSON object with the following structure:
        - riskScore: Overall risk score from 0-100 (higher = more likely to leave)
        - riskLevel: ONE of LOW (0-25), MEDIUM (26-50), HIGH (51-75), CRITICAL (76-100)
        - riskFactors: Array of factors increasing turnover risk
        - protectiveFactors: Array of factors reducing turnover risk
        - factorContributions: Object mapping factors to their contribution score (0-1)
        - retentionSuggestions: Array of retention actions with:
          - action: Description of the action
          - category: COMPENSATION, WORK_LIFE, GROWTH, MANAGEMENT, CULTURE
          - priority: 1-5 (1 = highest priority)
          - expectedImpact: Expected impact description
          - timeline: Suggested timeline
        - predictedTenureMonths: Predicted additional months before potential departure
        - confidence: Confidence level in the prediction (0-1)
        
        Be evidence-based and consider all provided data points in your analysis.
        """;

    public TurnoverPredictionResponse predictTurnover(TurnoverPredictionRequest request) {
        log.info("Predicting turnover risk for employee: {}", request.getEmployeeId());

        String userPrompt = buildPredictionPrompt(request);

        String response = deepSeekClient.chat(SYSTEM_PROMPT, userPrompt);

        try {
            return parseResponse(response, request);
        } catch (Exception e) {
            log.error("Failed to parse turnover prediction response: {}", e.getMessage());
            return createFallbackResponse(request);
        }
    }

    private String buildPredictionPrompt(TurnoverPredictionRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze this employee for turnover risk.\n\n");
        prompt.append("EMPLOYEE DATA:\n");
        prompt.append("Employee ID: ").append(request.getEmployeeId());
        prompt.append("\nDepartment: ").append(request.getDepartment());
        prompt.append("\nTenure: ").append(request.getTenureMonths()).append(" months");
        prompt.append("\nSatisfaction Score (1-10): ").append(request.getSatisfactionScore());
        prompt.append("\nPerformance Score (1-5): ").append(request.getPerformanceScore());
        prompt.append("\nSalary Ratio (vs market): ").append(request.getSalaryRatio());
        prompt.append("\nRecent Promotion: ").append(request.getRecentPromotion() == 1 ? "Yes" : "No");
        prompt.append("\nWork-Life Balance (1-10): ").append(request.getWorkLifeBalance());
        prompt.append("\nManager ID: ").append(request.getManagerId());
        prompt.append("\nTeam Size: ").append(request.getTeamSize());
        prompt.append("\nCommute Hours: ").append(request.getCommuteHours());
        
        if (request.getRecentAchievements() != null && !request.getRecentAchievements().isEmpty()) {
            prompt.append("\n\nRecent Achievements: ");
            prompt.append(String.join(", ", request.getRecentAchievements()));
        }
        
        if (request.getWarningSigns() != null && !request.getWarningSigns().isEmpty()) {
            prompt.append("\n\nWarning Signs: ");
            prompt.append(String.join(", ", request.getWarningSigns()));
        }
        
        prompt.append("\n\nReturn ONLY the JSON object, no additional text.");
        
        return prompt.toString();
    }

    private TurnoverPredictionResponse parseResponse(String response, TurnoverPredictionRequest request) throws JsonProcessingException {
        String cleanedResponse = cleanJsonResponse(response);
        JsonNode root = objectMapper.readTree(cleanedResponse);

        TurnoverPredictionResponse.TurnoverPredictionResponseBuilder builder = TurnoverPredictionResponse.builder()
                .employeeId(request.getEmployeeId())
                .riskScore(getDoubleValue(root, "riskScore", 50.0))
                .riskLevel(getTextValue(root, "riskLevel", "MEDIUM"))
                .riskFactors(parseStringArray(root, "riskFactors"))
                .protectiveFactors(parseStringArray(root, "protectiveFactors"))
                .predictedTenureMonths(root.has("predictedTenureMonths") ? 
                        root.get("predictedTenureMonths").asInt() : 12)
                .confidence(getDoubleValue(root, "confidence", 0.7));

        // Parse factor contributions
        if (root.has("factorContributions") && root.get("factorContributions").isObject()) {
            Map<String, Double> factorContributions = new HashMap<>();
            root.get("factorContributions").fields().forEachRemaining(f -> 
                factorContributions.put(f.getKey(), f.getValue().asDouble()));
            builder.factorContributions(factorContributions);
        }

        // Parse retention suggestions
        if (root.has("retentionSuggestions") && root.get("retentionSuggestions").isArray()) {
            List<TurnoverPredictionResponse.RetentionSuggestion> suggestions = new ArrayList<>();
            root.get("retentionSuggestions").forEach(node -> {
                suggestions.add(TurnoverPredictionResponse.RetentionSuggestion.builder()
                        .action(getTextValue(node, "action"))
                        .category(getTextValue(node, "category", "CULTURE"))
                        .priority(node.has("priority") ? node.get("priority").asInt() : 3)
                        .expectedImpact(getTextValue(node, "expectedImpact"))
                        .timeline(getTextValue(node, "timeline"))
                        .build());
            });
            builder.retentionSuggestions(suggestions);
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
        return getTextValue(node, field, null);
    }

    private String getTextValue(JsonNode node, String field, String defaultValue) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : defaultValue;
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

    private TurnoverPredictionResponse createFallbackResponse(TurnoverPredictionRequest request) {
        return TurnoverPredictionResponse.builder()
                .employeeId(request.getEmployeeId())
                .riskScore(50.0)
                .riskLevel("MEDIUM")
                .riskFactors(new ArrayList<>())
                .protectiveFactors(new ArrayList<>())
                .factorContributions(new HashMap<>())
                .retentionSuggestions(new ArrayList<>())
                .predictedTenureMonths(12)
                .confidence(0.0)
                .build();
    }
}

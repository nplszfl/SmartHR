package com.smarthr.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthr.ai.client.DeepSeekClient;
import com.smarthr.ai.dto.InterviewAnalysisRequest;
import com.smarthr.ai.dto.InterviewAnalysisResponse;
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
public class InterviewAnalysisService {

    private final DeepSeekClient deepSeekClient;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
        You are an expert HR interview analyst. Your task is to analyze interview transcripts and provide 
        comprehensive feedback on candidate performance.
        
        Analyze the interview and return a JSON object with the following structure:
        - overallScore: Overall interview score from 0-100
        - technicalAssessment: Object with score (0-100), strongAreas array, weakAreas array, technicalSkillsDemonstrated array
        - communicationAssessment: Object with score (0-100), clarity, articulation, communicationStrengths array, communicationWeaknesses array
        - behavioralAssessment: Object with score (0-100), competencies (key-value map), culturalFitIndicators array
        - strengths: Array of candidate strengths
        - concerns: Array of concerns
        - redFlags: Array of red flags (empty if none)
        - questionFeedback: Array of feedback for each question with question, answer, score, feedback
        - overallFeedback: Detailed overall feedback
        - hiringRecommendation: ONE of: STRONG_HIRE, HIRE, NO_HIRE, STRONG_NO_HIRE
        - confidence: Confidence level in the assessment (0-1)
        
        Be objective, specific, and evidence-based in your evaluation.
        """;

    public InterviewAnalysisResponse analyzeInterview(InterviewAnalysisRequest request) {
        log.info("Analyzing interview for candidate: {}", request.getCandidateName());

        String userPrompt = buildAnalysisPrompt(request);

        String response = deepSeekClient.chat(SYSTEM_PROMPT, userPrompt);

        try {
            return parseResponse(response, request);
        } catch (Exception e) {
            log.error("Failed to parse interview analysis response: {}", e.getMessage());
            return createFallbackResponse(request);
        }
    }

    private String buildAnalysisPrompt(InterviewAnalysisRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze this interview transcript and provide comprehensive feedback.\n\n");
        prompt.append("INTERVIEW TRANSCRIPT:\n").append(request.getTranscript());
        prompt.append("\n\nCANDIDATE: ").append(request.getCandidateName());
        prompt.append("\n\nPOSITION: ").append(request.getPosition());
        prompt.append("\n\nINTERVIEW TYPE: ").append(request.getInterviewType());
        prompt.append("\n\nDURATION: ").append(request.getInterviewDurationMinutes()).append(" minutes");
        
        if (request.getInterviewerNotes() != null && !request.getInterviewerNotes().isEmpty()) {
            prompt.append("\n\nINTERVIEWER NOTES: ").append(request.getInterviewerNotes());
        }
        
        prompt.append("\n\nReturn ONLY the JSON object, no additional text.");
        
        return prompt.toString();
    }

    private InterviewAnalysisResponse parseResponse(String response, InterviewAnalysisRequest request) throws JsonProcessingException {
        String cleanedResponse = cleanJsonResponse(response);
        JsonNode root = objectMapper.readTree(cleanedResponse);

        InterviewAnalysisResponse.InterviewAnalysisResponseBuilder builder = InterviewAnalysisResponse.builder()
                .candidateName(request.getCandidateName())
                .position(request.getPosition())
                .overallScore(getDoubleValue(root, "overallScore", 50.0))
                .overallFeedback(getTextValue(root, "overallFeedback"))
                .hiringRecommendation(getTextValue(root, "hiringRecommendation", "NO_HIRE"))
                .confidence(getDoubleValue(root, "confidence", 0.7))
                .strengths(parseStringArray(root, "strengths"))
                .concerns(parseStringArray(root, "concerns"))
                .redFlags(parseStringArray(root, "redFlags"));

        // Parse technical assessment
        if (root.has("technicalAssessment") && root.get("technicalAssessment").isObject()) {
            JsonNode techNode = root.get("technicalAssessment");
            builder.technicalAssessment(InterviewAnalysisResponse.TechnicalAssessment.builder()
                    .score(getDoubleValue(techNode, "score", 50.0))
                    .strongAreas(parseStringArray(techNode, "strongAreas"))
                    .weakAreas(parseStringArray(techNode, "weakAreas"))
                    .technicalSkillsDemonstrated(parseStringArray(techNode, "technicalSkillsDemonstrated"))
                    .build());
        }

        // Parse communication assessment
        if (root.has("communicationAssessment") && root.get("communicationAssessment").isObject()) {
            JsonNode commNode = root.get("communicationAssessment");
            builder.communicationAssessment(InterviewAnalysisResponse.CommunicationAssessment.builder()
                    .score(getDoubleValue(commNode, "score", 50.0))
                    .clarity(getTextValue(commNode, "clarity", "Average"))
                    .articulation(getTextValue(commNode, "articulation", "Average"))
                    .communicationStrengths(parseStringArray(commNode, "communicationStrengths"))
                    .communicationWeaknesses(parseStringArray(commNode, "communicationWeaknesses"))
                    .build());
        }

        // Parse behavioral assessment
        if (root.has("behavioralAssessment") && root.get("behavioralAssessment").isObject()) {
            JsonNode behavNode = root.get("behavioralAssessment");
            Map<String, String> competencies = new HashMap<>();
            if (behavNode.has("competencies") && behavNode.get("competencies").isObject()) {
                behavNode.get("competencies").fields().forEachRemaining(f -> 
                    competencies.put(f.getKey(), f.getValue().asText()));
            }
            builder.behavioralAssessment(InterviewAnalysisResponse.BehavioralAssessment.builder()
                    .score(getDoubleValue(behavNode, "score", 50.0))
                    .competencies(competencies)
                    .culturalFitIndicators(parseStringArray(behavNode, "culturalFitIndicators"))
                    .build());
        }

        // Parse question feedback
        if (root.has("questionFeedback") && root.get("questionFeedback").isArray()) {
            List<InterviewAnalysisResponse.QuestionFeedback> questionFeedback = new ArrayList<>();
            root.get("questionFeedback").forEach(node -> {
                questionFeedback.add(InterviewAnalysisResponse.QuestionFeedback.builder()
                        .question(getTextValue(node, "question"))
                        .answer(getTextValue(node, "answer"))
                        .score(getDoubleValue(node, "score", 50.0))
                        .feedback(getTextValue(node, "feedback"))
                        .build());
            });
            builder.questionFeedback(questionFeedback);
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

    private InterviewAnalysisResponse createFallbackResponse(InterviewAnalysisRequest request) {
        return InterviewAnalysisResponse.builder()
                .candidateName(request.getCandidateName())
                .position(request.getPosition())
                .overallScore(50.0)
                .technicalAssessment(InterviewAnalysisResponse.TechnicalAssessment.builder()
                        .score(50.0)
                        .strongAreas(new ArrayList<>())
                        .weakAreas(new ArrayList<>())
                        .technicalSkillsDemonstrated(new ArrayList<>())
                        .build())
                .communicationAssessment(InterviewAnalysisResponse.CommunicationAssessment.builder()
                        .score(50.0)
                        .clarity("Unable to assess")
                        .articulation("Unable to assess")
                        .communicationStrengths(new ArrayList<>())
                        .communicationWeaknesses(new ArrayList<>())
                        .build())
                .behavioralAssessment(InterviewAnalysisResponse.BehavioralAssessment.builder()
                        .score(50.0)
                        .competencies(new HashMap<>())
                        .culturalFitIndicators(new ArrayList<>())
                        .build())
                .strengths(new ArrayList<>())
                .concerns(new ArrayList<>())
                .redFlags(new ArrayList<>())
                .questionFeedback(new ArrayList<>())
                .overallFeedback("Service temporarily unavailable")
                .hiringRecommendation("NO_HIRE")
                .confidence(0.0)
                .build();
    }
}

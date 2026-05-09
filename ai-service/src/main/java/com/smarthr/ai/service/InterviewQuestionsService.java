package com.smarthr.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthr.ai.client.DeepSeekClient;
import com.smarthr.ai.dto.InterviewQuestionsRequest;
import com.smarthr.ai.dto.InterviewQuestionsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewQuestionsService {

    private final DeepSeekClient deepSeekClient;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
        You are an expert HR interview question designer. Your task is to generate relevant and effective 
        interview questions based on job descriptions.
        
        Generate interview questions and return a JSON object with the following structure:
        - questions: Array of question objects with:
          - number: Question number
          - category: Question category (TECHNICAL, BEHAVIORAL, SITUATIONAL, CULTURAL_FIT)
          - question: The actual question
          - type: OPEN_ENDED, SITUATIONAL, TECHNICAL, BEHAVIORAL
          - expectedAnswer: Brief description of expected answer
          - goodAnswerIndicators: Array of indicators of a good answer
          - poorAnswerIndicators: Array of indicators of a poor answer
          - maxScore: Maximum score for this question (typically 10)
        - evaluationCriteria: Array of overall evaluation criteria
        - estimatedDurationMinutes: Estimated interview duration
        
        Create questions that are relevant to the job description, varied in type, and effective at assessing candidates.
        """;

    public InterviewQuestionsResponse generateQuestions(InterviewQuestionsRequest request) {
        log.info("Generating {} interview questions for position type: {}", 
                request.getNumberOfQuestions() > 0 ? request.getNumberOfQuestions() : "default",
                request.getInterviewType());

        String userPrompt = buildQuestionsPrompt(request);

        String response = deepSeekClient.chat(SYSTEM_PROMPT, userPrompt);

        try {
            return parseResponse(response, request);
        } catch (Exception e) {
            log.error("Failed to parse questions response: {}", e.getMessage());
            return createFallbackResponse(request);
        }
    }

    private String buildQuestionsPrompt(InterviewQuestionsRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate interview questions for the following position.\n\n");
        prompt.append("JOB DESCRIPTION:\n").append(request.getJobDescription());
        prompt.append("\n\nINTERVIEW TYPE: ").append(request.getInterviewType());
        
        if (request.getCandidateBackground() != null && !request.getCandidateBackground().isEmpty()) {
            prompt.append("\n\nCANDIDATE BACKGROUND (for targeted questions): ").append(request.getCandidateBackground());
        }
        
        int numQuestions = request.getNumberOfQuestions() > 0 ? request.getNumberOfQuestions() : 5;
        prompt.append("\n\nNUMBER OF QUESTIONS: ").append(numQuestions);
        
        if (request.getFocusArea() != null && !request.getFocusArea().isEmpty()) {
            prompt.append("\n\nFOCUS AREA: ").append(request.getFocusArea());
        }
        
        prompt.append("\n\nReturn ONLY the JSON object, no additional text.");
        
        return prompt.toString();
    }

    private InterviewQuestionsResponse parseResponse(String response, InterviewQuestionsRequest request) throws JsonProcessingException {
        String cleanedResponse = cleanJsonResponse(response);
        JsonNode root = objectMapper.readTree(cleanedResponse);

        InterviewQuestionsResponse.InterviewQuestionsResponseBuilder builder = InterviewQuestionsResponse.builder()
                .jobTitle("Generated Questions")
                .interviewType(request.getInterviewType())
                .evaluationCriteria(parseStringArray(root, "evaluationCriteria"))
                .estimatedDurationMinutes(root.has("estimatedDurationMinutes") ? 
                        root.get("estimatedDurationMinutes").asInt() : 30);

        if (root.has("questions") && root.get("questions").isArray()) {
            List<InterviewQuestionsResponse.Question> questions = new ArrayList<>();
            root.get("questions").forEach(node -> {
                questions.add(InterviewQuestionsResponse.Question.builder()
                        .number(node.has("number") ? node.get("number").asInt() : questions.size() + 1)
                        .category(getTextValue(node, "category", "GENERAL"))
                        .question(getTextValue(node, "question"))
                        .type(getTextValue(node, "type", "OPEN_ENDED"))
                        .expectedAnswer(getTextValue(node, "expectedAnswer"))
                        .goodAnswerIndicators(parseStringArray(node, "goodAnswerIndicators"))
                        .poorAnswerIndicators(parseStringArray(node, "poorAnswerIndicators"))
                        .maxScore(node.has("maxScore") ? node.get("maxScore").asInt() : 10)
                        .build());
            });
            builder.questions(questions);
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

    private List<String> parseStringArray(JsonNode node, String field) {
        List<String> result = new ArrayList<>();
        if (node.has(field) && node.get(field).isArray()) {
            node.get(field).forEach(n -> result.add(n.asText()));
        }
        return result;
    }

    private InterviewQuestionsResponse createFallbackResponse(InterviewQuestionsRequest request) {
        List<InterviewQuestionsResponse.Question> fallbackQuestions = new ArrayList<>();
        fallbackQuestions.add(InterviewQuestionsResponse.Question.builder()
                .number(1)
                .category("GENERAL")
                .question("Tell me about yourself and your relevant experience.")
                .type("OPEN_ENDED")
                .expectedAnswer("A concise professional summary with relevant achievements")
                .goodAnswerIndicators(List.of("Relevant experience", "Clear communication", "Confident delivery"))
                .poorAnswerIndicators(List.of("Vague", "Irrelevant details", "Disorganized"))
                .maxScore(10)
                .build());

        return InterviewQuestionsResponse.builder()
                .jobTitle("Generated Questions")
                .interviewType(request.getInterviewType())
                .questions(fallbackQuestions)
                .evaluationCriteria(List.of("Relevance", "Clarity", "Specificity", "Confidence"))
                .estimatedDurationMinutes(30)
                .build();
    }
}

package com.smarthr.ai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeepSeekClient {

    private final WebClient deepSeekWebClient;
    private final ObjectMapper objectMapper;

    @Value("${deepseek.api.model}")
    private String model;

    @Value("${deepseek.api.max-retries}")
    private int maxRetries;

    public record ChatMessage(String role, String content) {}
    public record ChatRequest(String model, List<ChatMessage> messages, double temperature, int max_tokens) {}
    public record ChatResponse(String id, String object, long created, String model, List<Choice> choices, Usage usage) {}
    public record Choice(int index, Message message, String finish_reason) {}
    public record Message(String role, String content) {}
    public record Usage(int prompt_tokens, int completion_tokens, int total_tokens) {}

    @Retryable(
        retryFor = {WebClientResponseException.class},
        maxAttemptsExpression = "#{@deepSeekClient.maxRetries}",
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String chat(String systemPrompt, String userPrompt) {
        log.debug("Calling DeepSeek API with system prompt length: {} and user prompt length: {}",
                systemPrompt.length(), userPrompt.length());

        ChatRequest request = new ChatRequest(
                model,
                List.of(
                        new ChatMessage("system", systemPrompt),
                        new ChatMessage("user", userPrompt)
                ),
                0.7,
                4000
        );

        try {
            String response = deepSeekWebClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(60))
                    .block();

            return extractContent(response);
        } catch (WebClientResponseException e) {
            log.error("DeepSeek API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("DeepSeek API call failed: {}", e.getMessage());
            throw new RuntimeException("Failed to call DeepSeek API", e);
        }
    }

    @Retryable(
        retryFor = {WebClientResponseException.class},
        maxAttemptsExpression = "#{@deepSeekClient.maxRetries}",
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String chatWithMessages(List<ChatMessage> messages) {
        log.debug("Calling DeepSeek API with {} messages", messages.size());

        ChatRequest request = new ChatRequest(
                model,
                messages,
                0.7,
                4000
        );

        try {
            String response = deepSeekWebClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(60))
                    .block();

            return extractContent(response);
        } catch (WebClientResponseException e) {
            log.error("DeepSeek API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("DeepSeek API call failed: {}", e.getMessage());
            throw new RuntimeException("Failed to call DeepSeek API", e);
        }
    }

    public <T> T chatAndParse(String systemPrompt, String userPrompt, Class<T> clazz) {
        String jsonResponse = chat(systemPrompt, userPrompt);
        try {
            return objectMapper.readValue(jsonResponse, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON response: {}", jsonResponse);
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    public <T> T chatAndParse(String systemPrompt, String userPrompt, Class<T> clazz, T fallback) {
        try {
            return chatAndParse(systemPrompt, userPrompt, clazz);
        } catch (Exception e) {
            log.warn("Failed to parse AI response, returning fallback: {}", e.getMessage());
            return fallback;
        }
    }

    private String extractContent(String response) {
        try {
            ChatResponse chatResponse = objectMapper.readValue(response, ChatResponse.class);
            if (chatResponse.choices() != null && !chatResponse.choices().isEmpty()) {
                return chatResponse.choices().get(0).message().content();
            }
            throw new RuntimeException("No content in DeepSeek response");
        } catch (JsonProcessingException e) {
            log.error("Failed to parse DeepSeek response: {}", response);
            throw new RuntimeException("Failed to parse DeepSeek response", e);
        }
    }

    @Recover
    public String recoverFromWebClientException(WebClientResponseException e) {
        log.error("All retries exhausted. Final error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        return "{\"error\": \"AI service temporarily unavailable. Please try again later.\"}";
    }

    @Recover
    public String recoverFromException(Exception e) {
        log.error("All retries exhausted. Final error: {}", e.getMessage());
        return "{\"error\": \"AI service temporarily unavailable. Please try again later.\"}";
    }

    public boolean healthCheck() {
        try {
            chat("You are a helpful assistant.", "Respond with just 'OK' if you can hear me.");
            return true;
        } catch (Exception e) {
            log.error("Health check failed: {}", e.getMessage());
            return false;
        }
    }
}

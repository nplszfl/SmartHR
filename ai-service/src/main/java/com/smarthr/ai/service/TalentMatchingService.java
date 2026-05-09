package com.smarthr.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthr.ai.client.DeepSeekClient;
import com.smarthr.ai.dto.TalentMatchRequest;
import com.smarthr.ai.dto.TalentMatchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TalentMatchingService {

    private final DeepSeekClient deepSeekClient;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
        You are an expert HR talent matching specialist. Your task is to match candidates to jobs 
        based on skills, experience, and fit.
        
        Analyze the candidates and jobs, then return a JSON object with the following structure:
        - matches: Array of candidate-job matches, each with:
          - candidateId: Candidate identifier
          - candidateName: Candidate name
          - jobMatches: Array of job matches for this candidate, ordered by score, each with:
            - jobId: Job identifier
            - jobTitle: Job title
            - matchScore: Overall match score 0-100
            - matchLevel: ONE of PERFECT (90+), GOOD (70-89), MODERATE (50-69), WEAK (<50)
            - matchedSkills: Skills that match
            - missingSkills: Required skills missing
            - bonusSkills: Extra skills candidate has beyond requirements
            - skillScores: Object mapping each skill to individual match score (0-1)
            - recommendation: Brief match recommendation
        - totalCandidates: Total number of candidates processed
        - totalJobs: Total number of jobs processed
        
        Focus on skills matching, experience relevance, and overall fit.
        """;

    public TalentMatchResponse matchTalent(TalentMatchRequest request) {
        log.info("Matching {} candidates to {} jobs", 
                request.getCandidates().size(), request.getJobs().size());

        String userPrompt = buildMatchingPrompt(request);

        String response = deepSeekClient.chat(SYSTEM_PROMPT, userPrompt);

        try {
            return parseResponse(response, request);
        } catch (Exception e) {
            log.error("Failed to parse talent matching response: {}", e.getMessage());
            return createFallbackResponse(request);
        }
    }

    private String buildMatchingPrompt(TalentMatchRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Match candidates to jobs.\n\n");
        
        prompt.append("CANDIDATES:\n");
        for (TalentMatchRequest.CandidateProfile candidate : request.getCandidates()) {
            prompt.append("- ID: ").append(candidate.getCandidateId());
            prompt.append(", Name: ").append(candidate.getName());
            prompt.append(", Title: ").append(candidate.getCurrentTitle());
            prompt.append(", Skills: ").append(String.join(", ", candidate.getSkills()));
            prompt.append(", Experience: ").append(candidate.getYearsExperience()).append(" years");
            prompt.append(", Education: ").append(candidate.getEducationLevel());
            prompt.append(", Location: ").append(candidate.getLocation());
            if (candidate.getIndustries() != null) {
                prompt.append(", Industries: ").append(String.join(", ", candidate.getIndustries()));
            }
            prompt.append("\n");
        }
        
        prompt.append("\n\nJOBS:\n");
        for (TalentMatchRequest.JobProfile job : request.getJobs()) {
            prompt.append("- ID: ").append(job.getJobId());
            prompt.append(", Title: ").append(job.getTitle());
            prompt.append(", Department: ").append(job.getDepartment());
            prompt.append(", Required Skills: ").append(String.join(", ", job.getRequiredSkills()));
            if (job.getPreferredSkills() != null) {
                prompt.append(", Preferred Skills: ").append(String.join(", ", job.getPreferredSkills()));
            }
            prompt.append(", Min Experience: ").append(job.getMinYearsExperience()).append(" years");
            prompt.append(", Education: ").append(job.getEducationRequirement());
            prompt.append(", Location: ").append(job.getLocation());
            prompt.append("\n");
        }
        
        prompt.append("\n\nReturn ONLY the JSON object, no additional text.");
        
        return prompt.toString();
    }

    private TalentMatchResponse parseResponse(String response, TalentMatchRequest request) throws JsonProcessingException {
        String cleanedResponse = cleanJsonResponse(response);
        JsonNode root = objectMapper.readTree(cleanedResponse);

        TalentMatchResponse.TalentMatchResponseBuilder builder = TalentMatchResponse.builder()
                .totalCandidates(root.has("totalCandidates") ? root.get("totalCandidates").asInt() : 
                        request.getCandidates().size())
                .totalJobs(root.has("totalJobs") ? root.get("totalJobs").asInt() : 
                        request.getJobs().size());

        List<TalentMatchResponse.CandidateJobMatch> candidateMatches = new ArrayList<>();

        if (root.has("matches") && root.get("matches").isArray()) {
            root.get("matches").forEach(node -> {
                String candidateId = getTextValue(node, "candidateId");
                String candidateName = getTextValue(node, "candidateName");
                
                List<TalentMatchResponse.JobMatch> jobMatches = new ArrayList<>();
                if (node.has("jobMatches") && node.get("jobMatches").isArray()) {
                    node.get("jobMatches").forEach(jobNode -> {
                        TalentMatchResponse.JobMatch.JobMatchBuilder jobMatchBuilder = TalentMatchResponse.JobMatch.builder()
                                .jobId(getTextValue(jobNode, "jobId"))
                                .jobTitle(getTextValue(jobNode, "jobTitle"))
                                .matchScore(getDoubleValue(jobNode, "matchScore", 50.0))
                                .matchLevel(getTextValue(jobNode, "matchLevel"))
                                .matchedSkills(parseStringArray(jobNode, "matchedSkills"))
                                .missingSkills(parseStringArray(jobNode, "missingSkills"))
                                .bonusSkills(parseStringArray(jobNode, "bonusSkills"))
                                .recommendation(getTextValue(jobNode, "recommendation"));

                        if (jobNode.has("skillScores") && jobNode.get("skillScores").isObject()) {
                            Map<String, Double> skillScores = new HashMap<>();
                            jobNode.get("skillScores").fields().forEachRemaining(f -> 
                                skillScores.put(f.getKey(), f.getValue().asDouble()));
                            jobMatchBuilder.skillScores(skillScores);
                        }

                        jobMatches.add(jobMatchBuilder.build());
                    });
                }

                candidateMatches.add(TalentMatchResponse.CandidateJobMatch.builder()
                        .candidateId(candidateId)
                        .candidateName(candidateName)
                        .jobMatches(jobMatches)
                        .build());
            });
        }

        builder.matches(candidateMatches);
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

    private TalentMatchResponse createFallbackResponse(TalentMatchRequest request) {
        List<TalentMatchResponse.CandidateJobMatch> candidateMatches = new ArrayList<>();
        
        for (TalentMatchRequest.CandidateProfile candidate : request.getCandidates()) {
            List<TalentMatchResponse.JobMatch> jobMatches = new ArrayList<>();
            
            for (TalentMatchRequest.JobProfile job : request.getJobs()) {
                jobMatches.add(TalentMatchResponse.JobMatch.builder()
                        .jobId(job.getJobId())
                        .jobTitle(job.getTitle())
                        .matchScore(50.0)
                        .matchLevel("MODERATE")
                        .matchedSkills(new ArrayList<>())
                        .missingSkills(job.getRequiredSkills())
                        .bonusSkills(new ArrayList<>())
                        .skillScores(new HashMap<>())
                        .recommendation("Unable to generate recommendation")
                        .build());
            }
            
            candidateMatches.add(TalentMatchResponse.CandidateJobMatch.builder()
                    .candidateId(candidate.getCandidateId())
                    .candidateName(candidate.getName())
                    .jobMatches(jobMatches)
                    .build());
        }

        return TalentMatchResponse.builder()
                .matches(candidateMatches)
                .totalCandidates(request.getCandidates().size())
                .totalJobs(request.getJobs().size())
                .build();
    }
}

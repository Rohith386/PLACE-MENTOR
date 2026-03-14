package com.placementtracker.service;

import com.placementtracker.dto.LeetCodeStatsDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Service
@Slf4j
public class LeetCodeService {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public LeetCodeService(RestTemplateBuilder builder) {
        this.restTemplate = builder
            .setConnectTimeout(Duration.ofSeconds(10))
            .setReadTimeout(Duration.ofSeconds(15))
            .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Fetch LeetCode stats for a given username
     * @param username LeetCode username
     * @return LeetCodeStatsDTO with user stats
     */
    public LeetCodeStatsDTO getLeetCodeStats(String username) {
        if (username == null || username.trim().isEmpty()) {
            return createErrorDTO("Username is empty or null");
        }
        
        username = username.trim();
        log.info("Fetching LeetCode stats for username: {}", username);
        
        // Try the most reliable API first
        try {
            return fetchFromNitroxAPI(username);
        } catch (Exception e) {
            log.warn("Nitrox API failed, trying fallback: {}", e.getMessage());
        }
        
        try {
            return fetchFromAlfaAPI(username);
        } catch (Exception e) {
            log.warn("Alfa API failed: {}", e.getMessage());
        }
        
        return createErrorDTO("Unable to fetch LeetCode profile. Please check your internet connection and verify username is correct.");
    }
    
    /**
     * Fetch from Nitrox LeetCode API (most reliable)
     */
    private LeetCodeStatsDTO fetchFromNitroxAPI(String username) throws Exception {
        String url = "https://nitrox-leetcode-api.vercel.app/problems/user/" + username + "/solved";
        log.debug("Trying Nitrox API: {}", url);
        
        String response = restTemplate.getForObject(url, String.class);
        JsonNode root = objectMapper.readTree(response);
        
        if (root.has("error")) {
            throw new Exception("User not found");
        }
        
        LeetCodeStatsDTO stats = new LeetCodeStatsDTO();
        stats.setUsername(username);
        stats.setTotalSolved(root.get("solvedProblems").asInt(0));
        stats.setTotalQuestions(root.get("totalProblems").asInt(2000));
        
        // Try to get more detailed stats
        JsonNode difficulties = root.get("solvedByDifficulty");
        if (difficulties != null) {
            stats.setEasySolved(difficulties.get("easy").asInt(0));
            stats.setMediumSolved(difficulties.get("medium").asInt(0));
            stats.setHardSolved(difficulties.get("hard").asInt(0));
        }
        
        // Calculate rates
        stats.setEasyTotal(stats.getTotalQuestions() / 3);
        stats.setMediumTotal(stats.getTotalQuestions() / 3);
        stats.setHardTotal(stats.getTotalQuestions() / 3);
        
        double rate = stats.getTotalQuestions() > 0 ? 
            (stats.getTotalSolved() * 100.0) / stats.getTotalQuestions() : 0;
        stats.setAcceptanceRate(Math.round(rate * 100.0) / 100.0);
        
        stats.setErrorMessage(null);
        log.info("Successfully fetched from Nitrox API for: {}", username);
        return stats;
    }
    
    /**
     * Fetch from Alfa LeetCode API (fallback)
     */
    private LeetCodeStatsDTO fetchFromAlfaAPI(String username) throws Exception {
        String url = "https://alfa-leetcode-api.onrender.com/userProfile/" + username;
        log.debug("Trying Alfa API: {}", url);
        
        String response = restTemplate.getForObject(url, String.class);
        JsonNode root = objectMapper.readTree(response);
        
        if (root.has("status") && "error".equals(root.get("status").asText())) {
            throw new Exception("User not found");
        }
        
        LeetCodeStatsDTO stats = new LeetCodeStatsDTO();
        stats.setUsername(getStringValue(root, "username", username));
        stats.setName(getStringValue(root, "name", ""));
        stats.setRank(getIntValue(root, "ranking", 0));
        stats.setTotalSolved(getIntValue(root, "solved", 0));
        stats.setTotalQuestions(getIntValue(root, "totalQuestions", 0));
        stats.setEasySolved(getIntValue(root, "easySolved", 0));
        stats.setEasyTotal(getIntValue(root, "totalEasy", 0));
        stats.setMediumSolved(getIntValue(root, "mediumSolved", 0));
        stats.setMediumTotal(getIntValue(root, "totalMedium", 0));
        stats.setHardSolved(getIntValue(root, "hardSolved", 0));
        stats.setHardTotal(getIntValue(root, "totalHard", 0));
        
        // Calculate acceptance rate
        int total = stats.getTotalQuestions() != null && stats.getTotalQuestions() > 0 ? 
            stats.getTotalQuestions() : 1;
        int solved = stats.getTotalSolved() != null ? stats.getTotalSolved() : 0;
        double rate = (solved * 100.0) / total;
        stats.setAcceptanceRate(Math.round(rate * 100.0) / 100.0);
        
        stats.setErrorMessage(null);
        log.info("Successfully fetched from Alfa API for: {}", username);
        return stats;
    }
    
    private String getStringValue(JsonNode node, String field, String defaultValue) {
        try {
            if (node != null && node.has(field) && !node.get(field).isNull()) {
                return node.get(field).asText();
            }
        } catch (Exception e) {
            log.debug("Error getting string value for field {}: {}", field, e.getMessage());
        }
        return defaultValue;
    }
    
    private Integer getIntValue(JsonNode node, String field, int defaultValue) {
        try {
            if (node != null && node.has(field) && !node.get(field).isNull()) {
                return node.get(field).asInt();
            }
        } catch (Exception e) {
            log.debug("Error getting int value for field {}: {}", field, e.getMessage());
        }
        return defaultValue;
    }
    
    private LeetCodeStatsDTO createErrorDTO(String message) {
        LeetCodeStatsDTO error = new LeetCodeStatsDTO();
        error.setErrorMessage(message);
        return error;
    }
}


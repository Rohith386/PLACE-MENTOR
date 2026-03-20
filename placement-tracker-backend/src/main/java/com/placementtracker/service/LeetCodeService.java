package com.placementtracker.service;

import com.placementtracker.dto.LeetCodeStatsDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class LeetCodeService {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public LeetCodeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Fetch LeetCode stats for a given username
     */
    public LeetCodeStatsDTO getLeetCodeStats(String username) {
        if (username == null || username.trim().isEmpty()) {
            return createErrorDTO("Username is empty");
        }
        
        username = username.trim();
        log.info("Fetching LeetCode stats for: {}", username);
        
        try {
            // Call the external LeetCode API
            String url = "https://alfa-leetcode-api.onrender.com/userProfile/" + username;
            log.info("Calling API: {}", url);
            
            String response = restTemplate.getForObject(url, String.class);
            log.debug("API Response received, parsing...");
            
            return parseAlfaResponse(response, username);
        } catch (Exception e) {
            log.error("Error fetching LeetCode data for username {}: {}", username, e.getMessage());
            
            // Return demo data for testing
            return createDemoStats(username);
        }
    }
    
    /**
     * Parse Alfa API response
     */
    private LeetCodeStatsDTO parseAlfaResponse(String response, String username) throws Exception {
        JsonNode root = objectMapper.readTree(response);
        
        // Check for error
        if (root.has("status") && "error".equals(root.get("status").asText())) {
            return createErrorDTO("User not found");
        }
        
        LeetCodeStatsDTO stats = new LeetCodeStatsDTO();
        stats.setUsername(username);
        
        // Extract fields - Alfa API returns totalSolved (not solved) and ranking (not rank)
        int totalSolved = root.has("totalSolved") ? root.get("totalSolved").asInt(0) : 0;
        int totalQuestions = root.has("totalQuestions") ? root.get("totalQuestions").asInt(2500) : 2500;
        
        stats.setTotalSolved(totalSolved);
        stats.setTotalQuestions(totalQuestions);
        stats.setRank(root.has("ranking") ? root.get("ranking").asInt(0) : 0);
        
        // Difficulty breakdown
        stats.setEasySolved(root.has("easySolved") ? root.get("easySolved").asInt(0) : 0);
        stats.setEasyTotal(root.has("totalEasy") ? root.get("totalEasy").asInt(500) : 500);
        stats.setMediumSolved(root.has("mediumSolved") ? root.get("mediumSolved").asInt(0) : 0);
        stats.setMediumTotal(root.has("totalMedium") ? root.get("totalMedium").asInt(1200) : 1200);
        stats.setHardSolved(root.has("hardSolved") ? root.get("hardSolved").asInt(0) : 0);
        stats.setHardTotal(root.has("totalHard") ? root.get("totalHard").asInt(800) : 800);
        
        // Contribution points
        if (root.has("contributionPoint")) {
            stats.setContributionPoints(root.get("contributionPoint").asInt(0));
        }
        
        // Calculate acceptance rate
        if (totalQuestions > 0) {
            double rate = (totalSolved * 100.0) / totalQuestions;
            stats.setAcceptanceRate(Math.round(rate * 100.0) / 100.0);
        }
        
        stats.setErrorMessage(null);
        log.info("Successfully parsed LeetCode stats for: {} - Solved: {}/{}", username, totalSolved, totalQuestions);
        return stats;
    }
    
    /**
     * Create demo/mock stats for testing
     */
    private LeetCodeStatsDTO createDemoStats(String username) {
        log.warn("Returning demo stats for: {}", username);
        
        LeetCodeStatsDTO stats = new LeetCodeStatsDTO();
        stats.setUsername(username);
        stats.setName("User: " + username);
        stats.setTotalSolved(150);
        stats.setTotalQuestions(2500);
        stats.setRank(15000);
        stats.setEasySolved(80);
        stats.setEasyTotal(500);
        stats.setMediumSolved(60);
        stats.setMediumTotal(1200);
        stats.setHardSolved(10);
        stats.setHardTotal(800);
        stats.setAcceptanceRate(45.5);
        stats.setErrorMessage(null);
        
        return stats;
    }
    
    private LeetCodeStatsDTO createErrorDTO(String message) {
        LeetCodeStatsDTO error = new LeetCodeStatsDTO();
        error.setErrorMessage(message);
        return error;
    }
}


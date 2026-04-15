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
            // Try primary API first
            try {
                String response = restTemplate.getForObject(
                    "https://leetcode-api.vercel.app/" + username, String.class);
                if (response != null && !response.isEmpty()) {
                    return parseLeetCodeStatsResponse(response, username);
                }
            } catch (Exception e) {
                log.warn("Primary API failed, trying fallback: {}", e.getMessage());
            }
            
            // Try fallback API
            try {
                String response = restTemplate.getForObject(
                    "https://alfa-leetcode-api.onrender.com/userProfile/" + username, String.class);
                if (response != null && !response.isEmpty()) {
                    return parseAlfaResponse(response, username);
                }
            } catch (Exception e) {
                log.warn("Fallback API also failed: {}", e.getMessage());
            }
            
            // If all APIs fail, return error instead of demo data
            return createErrorDTO("Unable to fetch stats. Username may be invalid or LeetCode API is unavailable.");
            
        } catch (Exception e) {
            log.error("Error fetching LeetCode data for username {}: {}", username, e.getMessage());
            return createErrorDTO("Error fetching LeetCode stats: " + e.getMessage());
        }
    }
    
    /**
     * Parse LeetCode Stats API response (vercel.app)
     */
    private LeetCodeStatsDTO parseLeetCodeStatsResponse(String response, String username) throws Exception {
        JsonNode root = objectMapper.readTree(response);
        
        // Check for error conditions
        if (root.has("error") || (root.has("status") && !root.get("status").asText().equals("success"))) {
            return createErrorDTO("User not found");
        }
        
        LeetCodeStatsDTO stats = new LeetCodeStatsDTO();
        stats.setUsername(username);
        
        // Extract profile info
        if (root.has("realName")) {
            stats.setName(root.get("realName").asText());
        }
        if (root.has("avatar")) {
            stats.setAvatar(root.get("avatar").asText());
        }
        
        // Extract main stats - handle nested structure
        JsonNode matchedUser = root.has("matchedUser") ? root.get("matchedUser") : root;
        JsonNode profile = matchedUser.has("profile") ? matchedUser.get("profile") : matchedUser;
        JsonNode userCalendar = root.has("userCalendar") ? root.get("userCalendar") : null;
        
        // Ranking
        if (profile.has("ranking")) {
            stats.setRank(profile.get("ranking").asInt(0));
        }
        if (root.has("ranking")) {
            stats.setRank(root.get("ranking").asInt(0));
        }
        
        // Problem counts
        if (root.has("totalSolved")) {
            stats.setTotalSolved(root.get("totalSolved").asInt(0));
        }
        if (root.has("totalQuestions")) {
            stats.setTotalQuestions(root.get("totalQuestions").asInt(2700));
        } else {
            stats.setTotalQuestions(2700); // Current approx total on LeetCode
        }
        
        // Difficulty breakdown
        if (root.has("easySolved")) {
            stats.setEasySolved(root.get("easySolved").asInt(0));
        }
        if (root.has("easyTotal")) {
            stats.setEasyTotal(root.get("easyTotal").asInt(700));
        } else {
            stats.setEasyTotal(700);
        }
        
        if (root.has("mediumSolved")) {
            stats.setMediumSolved(root.get("mediumSolved").asInt(0));
        }
        if (root.has("mediumTotal")) {
            stats.setMediumTotal(root.get("mediumTotal").asInt(1500));
        } else {
            stats.setMediumTotal(1500);
        }
        
        if (root.has("hardSolved")) {
            stats.setHardSolved(root.get("hardSolved").asInt(0));
        }
        if (root.has("hardTotal")) {
            stats.setHardTotal(root.get("hardTotal").asInt(500));
        } else {
            stats.setHardTotal(500);
        }
        
        // Acceptance rate
        if (root.has("acceptanceRate")) {
            stats.setAcceptanceRate(root.get("acceptanceRate").asDouble(0.0));
        }
        
        // Contribution points
        if (root.has("contributionPoints")) {
            stats.setContributionPoints(root.get("contributionPoints").asInt(0));
        }
        
        stats.setErrorMessage(null);
        log.info("Successfully parsed LeetCode stats for: {} - Solved: {}/{}", username, stats.getTotalSolved(), stats.getTotalQuestions());
        return stats;
    }

    /**
     * Parse Alfa API response (fallback)
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
        int totalQuestions = root.has("totalQuestions") ? root.get("totalQuestions").asInt(2700) : 2700;
        
        stats.setTotalSolved(totalSolved);
        stats.setTotalQuestions(totalQuestions);
        stats.setRank(root.has("ranking") ? root.get("ranking").asInt(0) : 0);
        
        // Difficulty breakdown
        stats.setEasySolved(root.has("easySolved") ? root.get("easySolved").asInt(0) : 0);
        stats.setEasyTotal(root.has("totalEasy") ? root.get("totalEasy").asInt(700) : 700);
        stats.setMediumSolved(root.has("mediumSolved") ? root.get("mediumSolved").asInt(0) : 0);
        stats.setMediumTotal(root.has("totalMedium") ? root.get("totalMedium").asInt(1500) : 1500);
        stats.setHardSolved(root.has("hardSolved") ? root.get("hardSolved").asInt(0) : 0);
        stats.setHardTotal(root.has("totalHard") ? root.get("totalHard").asInt(500) : 500);
        
        // Contribution points
        if (root.has("contributionPoint")) {
            stats.setContributionPoints(root.get("contributionPoint").asInt(0));
        }
        
        // Calculate acceptance rate
        if (totalSolved > 0 && totalQuestions > 0) {
            double rate = (totalSolved * 100.0) / totalQuestions;
            stats.setAcceptanceRate(Math.round(rate * 100.0) / 100.0);
        }
        
        stats.setErrorMessage(null);
        log.info("Successfully parsed LeetCode stats (Alfa API) for: {} - Solved: {}/{}", username, totalSolved, totalQuestions);
        return stats;
    }
    
    
    private LeetCodeStatsDTO createErrorDTO(String message) {
        LeetCodeStatsDTO error = new LeetCodeStatsDTO();
        error.setErrorMessage(message);
        return error;
    }
}


package com.placementtracker.service;

import com.placementtracker.dto.LeetCodeStatsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class LeetCodeService {
    
    private static final String LEETCODE_API_URL = "https://leetcode-stats-api.herokuapp.com/";
    private final RestTemplate restTemplate;
    
    public LeetCodeService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Fetch LeetCode stats for a given username
     * @param username LeetCode username
     * @return LeetCodeStatsDTO with user stats
     */
    public LeetCodeStatsDTO getLeetCodeStats(String username) {
        if (username == null || username.trim().isEmpty()) {
            LeetCodeStatsDTO error = new LeetCodeStatsDTO();
            error.setErrorMessage("Username is empty or null");
            return error;
        }
        
        try {
            log.info("Fetching LeetCode stats for username: {}", username);
            String url = LEETCODE_API_URL + username;
            LeetCodeStatsDTO stats = restTemplate.getForObject(url, LeetCodeStatsDTO.class);
            log.info("Successfully fetched LeetCode stats for: {}", username);
            return stats;
        } catch (RestClientException e) {
            log.error("Error fetching LeetCode stats for username: {}", username, e);
            LeetCodeStatsDTO error = new LeetCodeStatsDTO();
            error.setErrorMessage("Failed to fetch LeetCode profile: " + e.getMessage());
            return error;
        } catch (Exception e) {
            log.error("Unexpected error fetching LeetCode stats: ", e);
            LeetCodeStatsDTO error = new LeetCodeStatsDTO();
            error.setErrorMessage("Unexpected error: " + e.getMessage());
            return error;
        }
    }
}

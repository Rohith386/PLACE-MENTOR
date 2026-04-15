package com.placementtracker.controller;

import com.placementtracker.dto.LeetCodeStatsDTO;
import com.placementtracker.entity.Student;
import com.placementtracker.repository.StudentRepository;
import com.placementtracker.service.LeetCodeService;
import com.placementtracker.service.TopicStrengthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/leetcode")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class LeetCodeController {
    
    private final LeetCodeService leetCodeService;
    private final TopicStrengthService topicStrengthService;
    private final StudentRepository studentRepository;
    
    public LeetCodeController(LeetCodeService leetCodeService, TopicStrengthService topicStrengthService, StudentRepository studentRepository) {
        this.leetCodeService = leetCodeService;
        this.topicStrengthService = topicStrengthService;
        this.studentRepository = studentRepository;
    }
    
    /**
     * Get LeetCode stats for current user
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getUserLeetCodeStats(
            @RequestHeader("X-Clerk-ID") String clerkId) {
        try {
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                LeetCodeStatsDTO error = new LeetCodeStatsDTO();
                error.setErrorMessage("Student profile not found. Please create your profile first.");
                return ResponseEntity.status(404).body(error);
            }
            
            String leetcodeUsername = student.get().getLeetcodeUsername();
            if (leetcodeUsername == null || leetcodeUsername.trim().isEmpty()) {
                LeetCodeStatsDTO response = new LeetCodeStatsDTO();
                response.setErrorMessage("LeetCode username not set");
                return ResponseEntity.status(400).body(response);
            }
            
            LeetCodeStatsDTO stats = leetCodeService.getLeetCodeStats(leetcodeUsername);
            if (stats.getErrorMessage() != null) {
                log.warn("LeetCode API error for user {}: {}", leetcodeUsername, stats.getErrorMessage());
                return ResponseEntity.badRequest().body(stats);
            }
            
            // Sync problems solved to Student entity
            Student s = student.get();
            s.setProblemsSolved(stats.getTotalSolved());
            studentRepository.save(s);
            
            // Auto-calculate topic strengths using ACTUAL LeetCode data
            topicStrengthService.calculateAndUpdateTopicStrengths(s, stats);
            log.info("Synced {} problems solved and calculated topic strengths using actual LeetCode data for student: {}", stats.getTotalSolved(), s.getId());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching LeetCode stats: ", e);
            LeetCodeStatsDTO error = new LeetCodeStatsDTO();
            error.setErrorMessage("Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get LeetCode stats for a specific username
     */
    @GetMapping("/stats/{username}")
    public ResponseEntity<?> getLeetCodeStatsByUsername(
            @PathVariable String username) {
        try {
            LeetCodeStatsDTO stats = leetCodeService.getLeetCodeStats(username);
            if (stats.getErrorMessage() != null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", stats.getErrorMessage());
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching LeetCode stats for {}: ", username, e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Update user's LeetCode username
     */
    @PostMapping("/username")
    public ResponseEntity<?> updateLeetCodeUsername(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestBody Map<String, String> request) {
        try {
            String leetcodeUsername = request.get("username");
            if (leetcodeUsername == null || leetcodeUsername.trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Username cannot be empty");
                return ResponseEntity.badRequest().body(response);
            }
            
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Student s = student.get();
            s.setLeetcodeUsername(leetcodeUsername);
            studentRepository.save(s);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "LeetCode username updated successfully");
            response.put("username", leetcodeUsername);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating LeetCode username: ", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Manually sync LeetCode stats (bypasses API rate limiting)
     * Request body: {"totalSolved": 336, "easySolved": 126, "mediumSolved": 178, "hardSolved": 32}
     */
    @PostMapping("/sync-manual")
    public ResponseEntity<?> manuallySyncLeetCodeStats(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestBody Map<String, Integer> request) {
        try {
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Student not found")
                );
            }

            int totalSolved = request.getOrDefault("totalSolved", 0);
            int easySolved = request.getOrDefault("easySolved", 0);
            int mediumSolved = request.getOrDefault("mediumSolved", 0);
            int hardSolved = request.getOrDefault("hardSolved", 0);

            if (totalSolved <= 0) {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "totalSolved must be greater than 0")
                );
            }

            // Create mock LeetCodeStatsDTO with the provided data
            LeetCodeStatsDTO stats = new LeetCodeStatsDTO();
            stats.setTotalSolved(totalSolved);
            stats.setTotalQuestions(2500);
            stats.setEasySolved(easySolved);
            stats.setEasyTotal(500);
            stats.setMediumSolved(mediumSolved);
            stats.setMediumTotal(1200);
            stats.setHardSolved(hardSolved);
            stats.setHardTotal(800);
            stats.setUsername(student.get().getLeetcodeUsername());

            // Sync to database
            Student s = student.get();
            s.setProblemsSolved(totalSolved);
            studentRepository.save(s);

            // Recalculate topic strengths with actual data
            topicStrengthService.calculateAndUpdateTopicStrengths(s, stats);

            log.info("Manually synced LeetCode stats for student {}: {} problems ({} easy, {} medium, {} hard)",
                s.getId(), totalSolved, easySolved, mediumSolved, hardSolved);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "LeetCode stats synced successfully");
            response.put("totalSolved", totalSolved);
            response.put("problemsSolved", s.getProblemsSolved());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error manually syncing LeetCode stats: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to sync stats: " + e.getMessage())
            );
        }
    }
}

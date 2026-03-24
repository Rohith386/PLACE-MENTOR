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
                Map<String, String> response = new HashMap<>();
                response.put("error", "Student profile not found. Please create your profile first.");
                response.put("message", "Visit /profile to set up your account");
                return ResponseEntity.status(404).body(response);
            }
            
            String leetcodeUsername = student.get().getLeetcodeUsername();
            if (leetcodeUsername == null || leetcodeUsername.trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "LeetCode username not set");
                return ResponseEntity.ok(response);
            }
            
            LeetCodeStatsDTO stats = leetCodeService.getLeetCodeStats(leetcodeUsername);
            if (stats.getErrorMessage() != null) {
                return ResponseEntity.badRequest().body(stats);
            }
            
            // Sync problems solved to Student entity
            Student s = student.get();
            s.setProblemsSolved(stats.getTotalSolved());
            studentRepository.save(s);
            
            // Auto-calculate topic strengths when fetching stats
            topicStrengthService.calculateAndUpdateTopicStrengths(s, stats.getTotalSolved());
            log.info("Synced {} problems solved and calculated topic strengths for student: {}", stats.getTotalSolved(), s.getId());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching LeetCode stats: ", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
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
}

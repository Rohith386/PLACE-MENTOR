package com.placementtracker.controller;

import com.placementtracker.dto.TopicStrengthDTO;
import com.placementtracker.entity.Student;
import com.placementtracker.repository.StudentRepository;
import com.placementtracker.service.TopicStrengthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topic-strength")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class TopicStrengthController {
    
    private final TopicStrengthService topicStrengthService;
    private final StudentRepository studentRepository;
    
    public TopicStrengthController(TopicStrengthService topicStrengthService, StudentRepository studentRepository) {
        this.topicStrengthService = topicStrengthService;
        this.studentRepository = studentRepository;
    }
    
    /**
     * Calculate and get topic strengths for current user
     * This analyzes their LeetCode problems and calculates strength per topic
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateTopicStrengths(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestParam(defaultValue = "0") int totalProblems) {
        try {
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Student not found")
                );
            }
            
            // If totalProblems is 0, use their LeetCode stats
            if (totalProblems == 0) {
                totalProblems = student.get().getProblemsSolved() != null ? 
                    student.get().getProblemsSolved() : 309; // Default to current user's count
            }
            
            List<TopicStrengthDTO> strengths = topicStrengthService
                .calculateAndUpdateTopicStrengths(student.get(), totalProblems);
            
            log.info("Calculated {} topic strengths for student: {}", strengths.size(), student.get().getId());
            return ResponseEntity.ok(strengths);
        } catch (Exception e) {
            log.error("Error calculating topic strengths: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to calculate strengths: " + e.getMessage())
            );
        }
    }
    
    /**
     * Get topic strengths for current user
     */
    @GetMapping("/my-strengths")
    public ResponseEntity<?> getMyTopicStrengths(
            @RequestHeader("X-Clerk-ID") String clerkId) {
        try {
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Student not found")
                );
            }
            
            List<TopicStrengthDTO> strengths = topicStrengthService.getTopicStrengths(student.get());
            
            if (strengths.isEmpty()) {
                log.info("No topic strengths found, calculating...");
                int totalProblems = student.get().getProblemsSolved() != null ? 
                    student.get().getProblemsSolved() : 309;
                strengths = topicStrengthService
                    .calculateAndUpdateTopicStrengths(student.get(), totalProblems);
            }
            
            return ResponseEntity.ok(strengths);
        } catch (Exception e) {
            log.error("Error fetching topic strengths: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to fetch strengths: " + e.getMessage())
            );
        }
    }
    
    /**
     * Get topic strengths by category
     */
    @GetMapping("/by-category/{category}")
    public ResponseEntity<?> getTopicStrengthsByCategory(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable String category) {
        try {
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Student not found")
                );
            }
            
            List<TopicStrengthDTO> strengths = topicStrengthService
                .getTopicStrengthsByCategory(student.get(), category);
            
            return ResponseEntity.ok(strengths);
        } catch (Exception e) {
            log.error("Error fetching topic strengths by category: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to fetch strengths: " + e.getMessage())
            );
        }
    }
}

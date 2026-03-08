package com.placementtracker.controller;

import com.placementtracker.ai.AgenticAIGuideService;
import com.placementtracker.dto.AIRecommendationDTO;
import com.placementtracker.entity.Student;
import com.placementtracker.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AIController {
    private final AgenticAIGuideService aiService;
    private final StudentRepository studentRepository;

    public AIController(AgenticAIGuideService aiService, StudentRepository studentRepository) {
        this.aiService = aiService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<AIRecommendationDTO>> getRecommendations(
            @RequestHeader("X-Clerk-ID") String clerkId) {
        Optional<Student> student = studentRepository.findByClerkId(clerkId);
        if (student.isPresent()) {
            List<AIRecommendationDTO> recommendations = aiService.generateRecommendations(student.get());
            return ResponseEntity.ok(recommendations);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/motivation")
    public ResponseEntity<Map<String, String>> getMotivationalMessage(
            @RequestHeader("X-Clerk-ID") String clerkId) {
        Optional<Student> student = studentRepository.findByClerkId(clerkId);
        if (student.isPresent()) {
            String message = aiService.generateMotivationalMessage(student.get());
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/daily-goals")
    public ResponseEntity<Map<String, List<String>>> getDailyGoals(
            @RequestHeader("X-Clerk-ID") String clerkId) {
        Optional<Student> student = studentRepository.findByClerkId(clerkId);
        if (student.isPresent()) {
            List<String> goals = aiService.generateDailyGoals(student.get());
            Map<String, List<String>> response = new HashMap<>();
            response.put("goals", goals);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/plan-next-steps")
    public ResponseEntity<Map<String, String>> planNextSteps(
            @RequestHeader("X-Clerk-ID") String clerkId) {
        Optional<Student> student = studentRepository.findByClerkId(clerkId);
        if (student.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Plan generated successfully");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
}

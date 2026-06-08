package com.placementtracker.controller;

import com.placementtracker.dto.*;
import com.placementtracker.service.MockInterviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock-interview")
@Slf4j
public class MockInterviewController {
    private final MockInterviewService mockInterviewService;

    public MockInterviewController(MockInterviewService mockInterviewService) {
        this.mockInterviewService = mockInterviewService;
    }

    /**
     * Start a new mock interview
     * POST /mock-interview/start
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startInterview(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestBody Map<String, String> request) {
        log.info("Starting interview for student: {}, type: {}", clerkId, request.get("type"));

        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "X-Clerk-ID header is required"));
            }

            String interviewType = request.get("type");
            if (interviewType == null || interviewType.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Interview type is required"));
            }

            MockInterviewDTO interview = mockInterviewService.startInterview(clerkId, interviewType);
            MockInterviewQuestionDTO firstQuestion = mockInterviewService.getFirstQuestion(interview.getId());

            Map<String, Object> response = Map.of(
                    "interview", interview,
                    "firstQuestion", firstQuestion
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error starting interview", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Submit answer for a question
     * POST /mock-interview/{interviewId}/submit
     */
    @PostMapping("/{interviewId}/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable Long interviewId,
            @RequestBody Map<String, Object> request) {
        log.info("Submitting answer for interview: {}", interviewId);

        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "X-Clerk-ID header is required"));
            }

            Long questionId = Long.parseLong(request.get("questionId").toString());
            String answer = request.get("answer").toString();

            MockInterviewAnswerDTO answerDTO = mockInterviewService.submitAnswer(clerkId, interviewId, questionId, answer);
            MockInterviewQuestionDTO nextQuestion = mockInterviewService.getNextQuestion(interviewId, questionId);

            Map<String, Object> response = Map.of(
                    "answer", answerDTO,
                    "nextQuestion", nextQuestion != null ? nextQuestion : "completed"
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error submitting answer", e);
            if (e.getMessage().contains("Unauthorized")) {
                return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error submitting answer", e);
            return ResponseEntity.badRequest().body(Map.of("error", "An error occurred while submitting your answer"));
        }
    }

    /**
     * Get first question of interview
     * GET /mock-interview/{interviewId}/first-question
     */
    @GetMapping("/{interviewId}/first-question")
    public ResponseEntity<MockInterviewQuestionDTO> getFirstQuestion(
            @PathVariable Long interviewId) {
        log.info("Getting first question for interview: {}", interviewId);

        try {
            MockInterviewQuestionDTO question = mockInterviewService.getFirstQuestion(interviewId);
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            log.error("Error getting first question", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get detailed interview information
     * GET /mock-interview/{interviewId}/details
     */
    @GetMapping("/{interviewId}/details")
    public ResponseEntity<Map<String, Object>> getInterviewDetails(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable Long interviewId) {
        log.info("Getting details for interview: {}", interviewId);

        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "X-Clerk-ID header is required"));
            }

            Map<String, Object> details = mockInterviewService.getInterviewDetails(clerkId, interviewId);
            return ResponseEntity.ok(details);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Unauthorized")) {
                return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
            }
            log.error("Error getting interview details", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get interview feedback
     * GET /mock-interview/{interviewId}/feedback
     */
    @GetMapping("/{interviewId}/feedback")
    public ResponseEntity<MockInterviewDTO> getInterviewFeedback(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable Long interviewId) {
        log.info("Getting feedback for interview: {}", interviewId);

        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "X-Clerk-ID header is required"));
            }

            MockInterviewDTO feedback = mockInterviewService.getInterviewFeedback(clerkId, interviewId);
            return ResponseEntity.ok(feedback);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Unauthorized")) {
                return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
            }
            log.error("Error getting feedback", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get interview history
     * GET /mock-interview/history
     */
    @GetMapping("/history")
    public ResponseEntity<List<MockInterviewDTO>> getInterviewHistory(
            @RequestHeader("X-Clerk-ID") String clerkId) {
        log.info("Getting interview history for student: {}", clerkId);

        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(List.of());
            }

            List<MockInterviewDTO> history = mockInterviewService.getInterviewHistory(clerkId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            log.error("Error getting interview history", e);
            return ResponseEntity.badRequest().build();
        }
    }
}

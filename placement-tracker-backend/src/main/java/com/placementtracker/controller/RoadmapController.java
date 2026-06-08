package com.placementtracker.controller;

import com.placementtracker.dto.DomainRoadmapDTO;
import com.placementtracker.dto.RoadmapDTO;
import com.placementtracker.entity.RoadmapTopic;
import com.placementtracker.entity.Student;
import com.placementtracker.repository.RoadmapTopicRepository;
import com.placementtracker.repository.StudentRepository;
import com.placementtracker.service.RoadmapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roadmap")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class RoadmapController {
    
    private static final List<String> VALID_DOMAINS = Arrays.asList(
        "fullstack", "full-stack",
        "ml", "machine-learning",
        "cybersecurity", "cyber-security",
        "cloud", "cloud-engineering",
        "devops"
    );
    
    private static final List<String> VALID_STATUSES = Arrays.asList(
        "not-started", "in-progress", "completed"
    );
    
    private final RoadmapService roadmapService;
    private final StudentRepository studentRepository;
    private final RoadmapTopicRepository roadmapTopicRepository;
    
    public RoadmapController(
            RoadmapService roadmapService,
            StudentRepository studentRepository,
            RoadmapTopicRepository roadmapTopicRepository) {
        this.roadmapService = roadmapService;
        this.studentRepository = studentRepository;
        this.roadmapTopicRepository = roadmapTopicRepository;
    }
    
    /**
     * Get domain-based roadmap for current student
     * @param clerkId Student's Clerk ID
     * @param domain Domain to generate roadmap for (fullstack, ml, cybersecurity, cloud, devops)
     */
    @GetMapping("/domain/{domain}")
    public ResponseEntity<?> getDomainRoadmap(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable String domain) {
        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "X-Clerk-ID header is required")
                );
            }

            if (!VALID_DOMAINS.contains(domain.toLowerCase())) {
                return ResponseEntity.status(400).body(
                    java.util.Map.of(
                        "error", "Unsupported domain: " + domain,
                        "validDomains", VALID_DOMAINS
                    )
                );
            }
            
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Student not found")
                );
            }
            
            DomainRoadmapDTO roadmap = roadmapService.generateRoadmapForDomain(student.get(), domain);
            
            if (roadmap.getPhases().isEmpty()) {
                return ResponseEntity.status(400).body(
                    java.util.Map.of("error", "Unsupported domain: " + domain)
                );
            }
            
            log.info("Generated roadmap for student {} with domain: {}", student.get().getId(), domain);
            return ResponseEntity.ok(roadmap);
        } catch (Exception e) {
            log.error("Error generating roadmap: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to generate roadmap: " + e.getMessage())
            );
        }
    }
    
    /**
     * Set student's preferred domain
     * @param clerkId Student's Clerk ID
     * @param domain Domain to set
     */
    @PostMapping("/set-domain")
    public ResponseEntity<?> setStudentDomain(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @RequestParam String domain) {
        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "X-Clerk-ID header is required")
                );
            }

            if (domain == null || domain.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "Domain parameter is required")
                );
            }

            if (!VALID_DOMAINS.contains(domain.toLowerCase())) {
                return ResponseEntity.status(400).body(
                    java.util.Map.of(
                        "error", "Invalid domain: " + domain,
                        "validDomains", VALID_DOMAINS
                    )
                );
            }
            
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Student not found")
                );
            }
            
            Student s = student.get();
            s.setDomain(domain);
            studentRepository.save(s);
            
            log.info("Set domain {} for student {}", domain, s.getId());
            return ResponseEntity.ok(java.util.Map.of(
                "message", "Domain set successfully",
                "domain", domain
            ));
        } catch (Exception e) {
            log.error("Error setting domain: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to set domain: " + e.getMessage())
            );
        }
    }
    
    /**
     * Get student's current domain
     * @param clerkId Student's Clerk ID
     */
    @GetMapping("/current-domain")
    public ResponseEntity<?> getCurrentDomain(
            @RequestHeader("X-Clerk-ID") String clerkId) {
        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "X-Clerk-ID header is required")
                );
            }

            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Student not found")
                );
            }
            
            String domain = student.get().getDomain();
            return ResponseEntity.ok(java.util.Map.of(
                "domain", domain != null ? domain : "not-set",
                "available_domains", new String[]{
                    "fullstack",
                    "ml",
                    "cybersecurity",
                    "cloud",
                    "devops"
                }
            ));
        } catch (Exception e) {
            log.error("Error getting current domain: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to get domain: " + e.getMessage())
            );
        }
    }
    
    /**
     * Get available domains
     */
    @GetMapping("/available-domains")
    public ResponseEntity<?> getAvailableDomains() {
        return ResponseEntity.ok(java.util.Map.of(
            "domains", new String[]{
                "fullstack",
                "ml",
                "cybersecurity",
                "cloud",
                "devops"
            },
            "descriptions", java.util.Map.of(
                "fullstack", "Full Stack Web Development",
                "ml", "Machine Learning Engineer",
                "cybersecurity", "Cybersecurity Professional",
                "cloud", "Cloud Engineer",
                "devops", "DevOps Engineer"
            )
        ));
    }
    
    /**
     * Update topic status
     * @param clerkId Student's Clerk ID
     * @param topicId Topic ID
     * @param status New status (not-started, in-progress, completed)
     */
    @PutMapping("/topic/{topicId}/status")
    public ResponseEntity<?> updateTopicStatus(
            @RequestHeader("X-Clerk-ID") String clerkId,
            @PathVariable Long topicId,
            @RequestParam String status) {
        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "X-Clerk-ID header is required")
                );
            }

            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "Status parameter is required")
                );
            }

            if (!VALID_STATUSES.contains(status.toLowerCase())) {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of(
                        "error", "Invalid status: " + status,
                        "validStatuses", VALID_STATUSES
                    )
                );
            }
            
            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Student not found")
                );
            }
            
            Optional<RoadmapTopic> topic = roadmapTopicRepository.findById(topicId);
            if (topic.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Topic not found")
                );
            }
            
            RoadmapTopic t = topic.get();
            
            // Verify the topic belongs to this student
            if (!t.getStudent().getId().equals(student.get().getId())) {
                return ResponseEntity.status(403).body(
                    java.util.Map.of("error", "Unauthorized: Topic does not belong to this student")
                );
            }
            
            t.setStatus(status);
            
            if ("completed".equalsIgnoreCase(status)) {
                t.setCompletionPercentage(100);
            } else if ("in-progress".equalsIgnoreCase(status)) {
                t.setCompletionPercentage(50);
            } else {
                t.setCompletionPercentage(0);
            }
            
            roadmapTopicRepository.save(t);
            
            log.info("Updated topic {} status to {} for student {}", topicId, status, student.get().getId());
            return ResponseEntity.ok(java.util.Map.of(
                "message", "Topic status updated",
                "topicId", topicId,
                "status", status
            ));
        } catch (Exception e) {
            log.error("Error updating topic status: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to update status: " + e.getMessage())
            );
        }
    }
            return ResponseEntity.ok(java.util.Map.of(
                "message", "Topic status updated",
                "topicId", topicId,
                "status", status
            ));
        } catch (Exception e) {
            log.error("Error updating topic status: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to update status: " + e.getMessage())
            );
        }
    }
    
    /**
     * Get roadmap progress for current domain
     * @param clerkId Student's Clerk ID
     */
    @GetMapping("/progress")
    public ResponseEntity<?> getRoadmapProgress(
            @RequestHeader("X-Clerk-ID") String clerkId) {
        try {
            if (clerkId == null || clerkId.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "X-Clerk-ID header is required")
                );
            }

            Optional<Student> student = studentRepository.findByClerkId(clerkId);
            if (student.isEmpty()) {
                return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Student not found")
                );
            }
            
            if (student.get().getDomain() == null || student.get().getDomain().isEmpty()) {
                return ResponseEntity.ok(java.util.Map.of(
                    "domain", "not-set",
                    "completionPercentage", 0,
                    "message", "No domain selected"
                ));
            }
            
            DomainRoadmapDTO roadmap = roadmapService.generateRoadmapForDomain(
                student.get(),
                student.get().getDomain()
            );
            
            return ResponseEntity.ok(java.util.Map.of(
                "domain", roadmap.getDomain(),
                "completionPercentage", roadmap.getCompletionPercentage(),
                "completedTopics", roadmap.getCompletedTopics(),
                "totalTopics", roadmap.getTotalTopics(),
                "estimatedDuration", roadmap.getEstimatedDuration()
            ));
        } catch (Exception e) {
            log.error("Error getting roadmap progress: ", e);
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "Failed to get progress: " + e.getMessage())
            );
        }
    }
}

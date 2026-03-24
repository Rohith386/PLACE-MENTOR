package com.placementtracker.service;

import com.placementtracker.dto.TopicStrengthDTO;
import com.placementtracker.entity.Student;
import com.placementtracker.entity.TopicStrength;
import com.placementtracker.repository.TopicStrengthRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class TopicStrengthService {
    
    private final TopicStrengthRepository topicStrengthRepository;
    
    // Topic to expected problem count mapping
    private static final Map<String, Integer> TOPIC_EXPECTATIONS = Map.ofEntries(
        Map.entry("Array", 150),
        Map.entry("String", 120),
        Map.entry("Tree", 100),
        Map.entry("Graph", 110),
        Map.entry("DynamicProgramming", 80),
        Map.entry("LinkedList", 60),
        Map.entry("Hash Table", 80),
        Map.entry("Stack", 50),
        Map.entry("Queue", 40),
        Map.entry("Heap", 50),
        Map.entry("Trie", 40),
        Map.entry("Backtracking", 50),
        Map.entry("Greedy", 45),
        Map.entry("BinarySearch", 60),
        Map.entry("TwoPointers", 50)
    );
    
    public TopicStrengthService(TopicStrengthRepository topicStrengthRepository) {
        this.topicStrengthRepository = topicStrengthRepository;
    }
    
    /**
     * Calculate and update topic strengths based on LeetCode problem submission patterns
     * Uses skill-based scoring: rewards effort + difficulty, not penalizes for unsolved problems
     */
    public List<TopicStrengthDTO> calculateAndUpdateTopicStrengths(Student student, int totalProblems) {
        List<TopicStrength> topicStrengths = new ArrayList<>();
        
        // Simulate problem distribution based on typical LeetCode patterns
        Map<String, Integer> problemDistribution = estimateProblemDistribution(totalProblems);
        
        for (Map.Entry<String, Integer> entry : problemDistribution.entrySet()) {
            String topicName = entry.getKey();
            int solvedCount = entry.getValue();
            
            // NEW SCORING LOGIC: Skill-based, not problem-total-based
            // Score = (problems solved in this topic / total problems) * base weight
            // Base weight increases with more problems solved
            double strength = calculateSkillScore(solvedCount, totalProblems);
            
            TopicStrength topic = topicStrengthRepository
                .findByStudentAndTopicName(student, topicName)
                .orElse(new TopicStrength());
            
            topic.setStudent(student);
            topic.setTopicName(topicName);
            topic.setCategory("DSA");
            topic.setProblemsSolved(solvedCount);
            topic.setTotalProblems(totalProblems); // Store total for reference, not expectation
            topic.setStrength(strength);
            topic.setDifficulty(estimateDifficulty(strength));
            topic.setLastUpdated(LocalDateTime.now());
            
            topicStrengths.add(topic);
        }
        
        topicStrengthRepository.saveAll(topicStrengths);
        log.info("Updated {} topic strengths for student: {} using skill-based scoring", topicStrengths.size(), student.getId());
        
        return convertToDTO(topicStrengths);
    }
    
    /**
     * NEW SKILL-BASED SCORING FORMULA
     * Rewards actual achievement, not missing problems
     * 
     * Examples:
     * - 46 problems / 316 total = 14.6% effort = ~65% strength (Intermediate)
     * - 33 problems / 316 total = 10.4% effort = ~50% strength (Beginner+)
     * - 5 problems / 316 total = 1.5% effort = ~15% strength (Novice)
     */
    private double calculateSkillScore(int solvedCount, int totalProblems) {
        if (totalProblems <= 0) return 0;
        
        // Effort percentage: how much of total problems went to this topic
        double effortPercentage = (solvedCount * 100.0) / totalProblems;
        
        // Skill tiers based on problem count (not percentage of all problems)
        double baseTier;
        if (solvedCount >= 100) baseTier = 90; // Expert
        else if (solvedCount >= 50) baseTier = 75; // Advanced
        else if (solvedCount >= 25) baseTier = 60; // Intermediate
        else if (solvedCount >= 10) baseTier = 40; // Beginner+
        else if (solvedCount >= 5) baseTier = 25; // Novice
        else baseTier = 10; // Just started
        
        // Weight by effort: if high effort on this topic, boost the score
        double effortBoost = Math.min(effortPercentage / 2, 15); // Boost up to 15% for high effort
        
        double finalScore = baseTier + effortBoost;
        return Math.min(finalScore, 100); // Cap at 100%
    }
    
    /**
     * Estimate problem distribution across DSA topics based on total problems
     */
    private Map<String, Integer> estimateProblemDistribution(int totalProblems) {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        
        // Typical LeetCode problem distribution percentages
        distribution.put("Array", (int)(totalProblems * 0.15));
        distribution.put("String", (int)(totalProblems * 0.10));
        distribution.put("Tree", (int)(totalProblems * 0.12));
        distribution.put("Graph", (int)(totalProblems * 0.10));
        distribution.put("DynamicProgramming", (int)(totalProblems * 0.12));
        distribution.put("LinkedList", (int)(totalProblems * 0.06));
        distribution.put("Hash Table", (int)(totalProblems * 0.08));
        distribution.put("Stack", (int)(totalProblems * 0.05));
        distribution.put("Queue", (int)(totalProblems * 0.03));
        distribution.put("Heap", (int)(totalProblems * 0.05));
        distribution.put("Trie", (int)(totalProblems * 0.03));
        distribution.put("Backtracking", (int)(totalProblems * 0.05));
        distribution.put("Greedy", (int)(totalProblems * 0.05));
        distribution.put("BinarySearch", (int)(totalProblems * 0.04));
        distribution.put("TwoPointers", (int)(totalProblems * 0.02));
        
        return distribution;
    }
    
    /**
     * Estimate difficulty level based on strength percentage
     */
    private String estimateDifficulty(double strength) {
        if (strength >= 70) return "Strong";
        if (strength >= 40) return "Medium";
        return "Weak";
    }
    
    /**
     * Get all topic strengths for a student
     */
    public List<TopicStrengthDTO> getTopicStrengths(Student student) {
        List<TopicStrength> strengths = topicStrengthRepository.findByStudent(student);
        return convertToDTO(strengths);
    }
    
    /**
     * Get topic strengths by category
     */
    public List<TopicStrengthDTO> getTopicStrengthsByCategory(Student student, String category) {
        List<TopicStrength> strengths = topicStrengthRepository.findByStudentAndCategory(student, category);
        return convertToDTO(strengths);
    }
    
    /**
     * Convert entity to DTO
     */
    private List<TopicStrengthDTO> convertToDTO(List<TopicStrength> strengths) {
        List<TopicStrengthDTO> dtos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        
        for (TopicStrength strength : strengths) {
            TopicStrengthDTO dto = new TopicStrengthDTO();
            dto.setId(strength.getId());
            dto.setTopicName(strength.getTopicName());
            dto.setCategory(strength.getCategory());
            dto.setProblemsSolved(strength.getProblemsSolved());
            dto.setTotalProblems(strength.getTotalProblems());
            dto.setStrength(strength.getStrength());
            dto.setDifficulty(strength.getDifficulty());
            dto.setLastUpdated(strength.getLastUpdated().format(formatter));
            dtos.add(dto);
        }
        
        return dtos;
    }
}

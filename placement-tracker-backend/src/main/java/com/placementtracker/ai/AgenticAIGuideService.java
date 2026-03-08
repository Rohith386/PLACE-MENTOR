package com.placementtracker.ai;

import com.placementtracker.dto.AIRecommendationDTO;
import com.placementtracker.entity.Student;
import com.placementtracker.entity.MockInterview;
import com.placementtracker.repository.RoadmapTopicRepository;
import com.placementtracker.repository.MockInterviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AgenticAIGuideService {
    private final RoadmapTopicRepository roadmapTopicRepository;
    private final MockInterviewRepository mockInterviewRepository;

    public AgenticAIGuideService(RoadmapTopicRepository roadmapTopicRepository,
                                   MockInterviewRepository mockInterviewRepository) {
        this.roadmapTopicRepository = roadmapTopicRepository;
        this.mockInterviewRepository = mockInterviewRepository;
    }

    /**
     * Agentic AI: Analyzes student progress and generates personalized recommendations
     */
    public List<AIRecommendationDTO> generateRecommendations(Student student) {
        List<AIRecommendationDTO> recommendations = new ArrayList<>();

        // Analyze roadmap progress
        var roadmapTopics = roadmapTopicRepository.findByStudent(student);
        var incompleteTopics = roadmapTopics.stream()
            .filter(t -> !t.getStatus().equals("completed"))
            .toList();

        if (!incompleteTopics.isEmpty()) {
            var nextTopic = incompleteTopics.get(0);
            recommendations.add(new AIRecommendationDTO(
                "Study: " + nextTopic.getTopicName(),
                "Complete this topic to improve your overall readiness",
                nextTopic.getPriority(),
                "topic"
            ));
        }

        // Suggest problems based on weak areas
        recommendations.add(new AIRecommendationDTO(
            "Practice 5 medium-level DSA problems",
            "Your DSA accuracy is 65%. Practice more to reach 85%",
            "high",
            "problem"
        ));

        // Suggest projects
        recommendations.add(new AIRecommendationDTO(
            "Build a Full-Stack Todo App",
            "Create a project to strengthen your portfolio",
            "medium",
            "project"
        ));

        // Mock interview suggestion
        var mockInterviews = mockInterviewRepository.findByStudent(student);
        if (mockInterviews.size() < 3) {
            recommendations.add(new AIRecommendationDTO(
                "Attend a mock interview",
                "You need 3+ mock interviews. Schedule one today!",
                "high",
                "topic"
            ));
        }

        return recommendations;
    }

    /**
     * Agentic AI: Generates motivational messages based on student progress
     */
    public String generateMotivationalMessage(Student student) {
        int readinessScore = student.getReadinessScore();
        int problemsSolved = student.getProblemsSolved();
        int mockInterviews = student.getMockInterviews();

        if (readinessScore >= 80) {
            return "You're almost there! 🚀 Your preparation is outstanding. Keep pushing for that dream company!";
        } else if (readinessScore >= 60) {
            return "You're doing great! 💪 You've solved " + problemsSolved + " problems. Keep the momentum going!";
        } else if (readinessScore >= 40) {
            return "Good progress so far! 📈 Attend " + (3 - mockInterviews) + " more mock interviews to boost your confidence.";
        } else {
            return "Every expert was once a beginner! 🌱 Start with the basics and build your way up.";
        }
    }

    /**
     * Agentic AI: Calculates dynamic company crack probability
     */
    public Integer calculateCompanyProbability(Student student, Integer baseCompanyProbability) {
        int readinessScore = student.getReadinessScore();
        int mockInterviewScore = getMockInterviewAverageScore(student);

        // Algorithm: Combine readiness + mock interview performance
        double calculatedProbability = (readinessScore * 0.6) + (mockInterviewScore * 0.4);
        calculatedProbability = Math.min(calculatedProbability, 100);
        calculatedProbability = Math.max(calculatedProbability, 10);

        return (int) calculatedProbability;
    }

    /**
     * Helper: Get average mock interview score
     */
    private Integer getMockInterviewAverageScore(Student student) {
        var interviews = mockInterviewRepository.findByStudent(student);
        if (interviews.isEmpty()) return 0;
        return (int) interviews.stream()
            .mapToInt(MockInterview::getScore)
            .average()
            .orElse(0);
    }

    /**
     * Agentic AI: Generate daily actionable goals
     */
    public List<String> generateDailyGoals(Student student) {
        List<String> goals = new ArrayList<>();

        goals.add("✓ Solve 3 DSA problems");
        goals.add("✓ Review 1 CS Fundamental concept");
        goals.add("✓ Spend 30 mins on weak area");

        if (student.getMockInterviews() < 2) {
            goals.add("✓ Schedule mock interview");
        }

        return goals;
    }
}

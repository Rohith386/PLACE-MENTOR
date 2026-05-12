package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapTopicDTO {
    private Long id;
    private String topicName;
    private String category;
    private String description;
    private String priority; // high, medium, low
    private String status; // not-started, in-progress, completed
    private Integer completionPercentage;
    private List<String> resources; // Learning resources URLs
    private List<String> practiceProblems; // LeetCode or problem URLs
}

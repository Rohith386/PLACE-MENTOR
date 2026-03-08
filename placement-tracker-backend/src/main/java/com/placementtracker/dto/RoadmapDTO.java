package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapDTO {
    private Long studentId;
    private List<RoadmapCategoryDTO> categories;
    private Integer overallProgress;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RoadmapCategoryDTO {
    private String name;
    private String emoji;
    private Integer completedTopics;
    private Integer totalTopics;
    private List<RoadmapTopicItemDTO> topics;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RoadmapTopicItemDTO {
    private Long id;
    private String name;
    private String status;
    private String priority;
    private Integer completionPercentage;
}

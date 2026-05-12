package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainRoadmapDTO {
    private String domain;
    private String title;
    private String description;
    private String skillLevel; // beginner, intermediate, advanced
    private Integer estimatedDuration; // in weeks
    private Integer completionPercentage;
    private Integer totalTopics;
    private Integer completedTopics;
    private List<RoadmapPhaseDTO> phases;
}


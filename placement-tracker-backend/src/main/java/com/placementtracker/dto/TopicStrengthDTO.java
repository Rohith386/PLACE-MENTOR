package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicStrengthDTO {
    private Long id;
    private String topicName;
    private String category;
    private Integer problemsSolved;
    private Integer totalProblems;
    private Double strength; // 0-100
    private String difficulty;
    private String lastUpdated;
}

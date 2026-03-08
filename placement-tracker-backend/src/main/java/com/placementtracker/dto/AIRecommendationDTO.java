package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRecommendationDTO {
    private String title;
    private String description;
    private String priority; // high, medium, low
    private String type; // topic, problem, project
}

package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapPhaseDTO {
    private String phaseName;
    private String description;
    private Integer weekDuration;
    private Integer order;
    private List<RoadmapTopicDTO> topics;
}

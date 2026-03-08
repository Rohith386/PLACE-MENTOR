package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String branch;
    private Integer year;
    private String skillLevel;
    private Integer readinessScore;
    private Integer topicsCompleted;
    private Integer problemsSolved;
    private Integer mockInterviews;
    private LocalDateTime createdAt;
}

package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockInterviewDTO {
    private Long id;
    private String type; // dsa, behavioral, hr
    private Integer score;
    private String feedback;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer totalQuestions;
    private Integer answeredQuestions;
    private String status; // in-progress, completed
}

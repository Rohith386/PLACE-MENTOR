package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockInterviewQuestionDTO {
    private Long id;
    private Long interviewId;
    private Integer questionNumber;
    private String type; // dsa, behavioral, hr
    private String question;
    private String context;
    private Integer difficulty; // 1-5
}

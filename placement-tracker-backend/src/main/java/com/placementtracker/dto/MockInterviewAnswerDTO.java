package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockInterviewAnswerDTO {
    private Long id;
    private Long questionId;
    private Long interviewId;
    private String studentAnswer;
    private Integer scoreObtained;
    private String aiAnalysis;
    private String suggestions;
    private Long submittedAt;
}

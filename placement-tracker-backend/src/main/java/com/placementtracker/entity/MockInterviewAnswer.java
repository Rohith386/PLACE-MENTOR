package com.placementtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mock_interview_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockInterviewAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private MockInterviewQuestion question;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private MockInterview interview;

    private String studentAnswer;
    private Integer scoreObtained = 0;
    private String aiAnalysis;
    private String suggestions;

    private Long submittedAt = System.currentTimeMillis();
}

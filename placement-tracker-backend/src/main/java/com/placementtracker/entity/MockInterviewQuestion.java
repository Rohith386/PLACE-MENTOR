package com.placementtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "mock_interview_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockInterviewQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private MockInterview interview;

    private Integer questionNumber;
    private String type; // dsa, behavioral, hr
    private String question;
    private String context;
    private String expectedAnswer;
    private Integer difficulty; // 1-5

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

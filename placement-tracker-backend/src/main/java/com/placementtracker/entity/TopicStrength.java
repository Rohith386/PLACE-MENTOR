package com.placementtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "topic_strengths")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicStrength {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String topicName; // Array, String, Tree, Graph, DP, etc.
    private String category; // DSA, CS_Fundamentals, etc.
    
    private Integer problemsSolved; // count of problems solved
    private Integer totalProblems;  // expected or total available
    private Double strength; // percentage 0-100
    private String difficulty; // Easy, Medium, Hard average
    
    private LocalDateTime lastUpdated = LocalDateTime.now();
}

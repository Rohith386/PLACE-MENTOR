package com.placementtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String clerkId;

    private String firstName;
    private String lastName;
    private String email;

    private String branch;
    private Integer year;
    private String skillLevel;

    private Integer readinessScore = 0;
    private Integer topicsCompleted = 0;
    private Integer problemsSolved = 0;
    private Integer mockInterviews = 0;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}

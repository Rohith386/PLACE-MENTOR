package com.placementtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roadmap_topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String category;
    private String topicName;
    private String status; // not-started, in-progress, completed
    private String priority; // high, medium, low

    private Integer completionPercentage = 0;
}

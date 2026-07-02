package com.placementtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    public static StudentDTO empty() {
        StudentDTO dto = new StudentDTO();
        dto.setFirstName("");
        dto.setLastName("");
        dto.setEmail("");
        dto.setBranch("");
        dto.setYear(0);
        dto.setSkillLevel("beginner");
        dto.setLeetcodeUsername("");
        dto.setCodeforcesUsername("");
        dto.setGithubUsername("");
        dto.setReadinessScore(0);
        dto.setTopicsCompleted(0);
        dto.setProblemsSolved(0);
        dto.setMockInterviews(0);
        dto.setDomain("");
        return dto;
    }
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String branch;
    private Integer year;
    private String skillLevel;
    private String leetcodeUsername;
    private String codeforcesUsername;
    private String githubUsername;
    private Integer readinessScore;
    private Integer topicsCompleted;
    private Integer problemsSolved;
    private Integer mockInterviews;
    private String domain;
    private LocalDateTime createdAt;
}

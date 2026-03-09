package com.placementtracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeetCodeStatsDTO {
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("avatar")
    private String avatar;
    
    @JsonProperty("rank")
    private Integer rank;
    
    @JsonProperty("totalSolved")
    private Integer totalSolved;
    
    @JsonProperty("totalQuestions")
    private Integer totalQuestions;
    
    @JsonProperty("easySolved")
    private Integer easySolved;
    
    @JsonProperty("easyTotal")
    private Integer easyTotal;
    
    @JsonProperty("mediumSolved")
    private Integer mediumSolved;
    
    @JsonProperty("mediumTotal")
    private Integer mediumTotal;
    
    @JsonProperty("hardSolved")
    private Integer hardSolved;
    
    @JsonProperty("hardTotal")
    private Integer hardTotal;
    
    @JsonProperty("acceptanceRate")
    private Double acceptanceRate;
    
    @JsonProperty("contributionPoints")
    private Integer contributionPoints;
    
    private String errorMessage;
}

package com.coderscenter.backend.dtos.optimization;

import com.coderscenter.backend.optaplanner.domain.SlotAssignment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationJobResponse {
    
    private String jobId;
    private String status; // "running", "completed", "failed", "cancelled"
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private Integer totalLessons;
    private Integer totalTrainers;
    private Integer progress; // 0-100
    private Boolean feasible;
    private String score;
    private String message;
    private Integer primaryTrainerAssignments;
    private Double primaryTrainerPercentage;
    private String fullReport;
    
    // OptaPlanner solution data (not serialized to frontend)
    @JsonIgnore
    private List<SlotAssignment> optimizedSlotAssignments;
}

package com.coderscenter.backend.dtos.optimization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationRequest {
    
    private Long scheduleId;
    private Integer maxRunTimeMinutes; // Default: 5 minutes
    private String optimizationGoal; // "BALANCE_WORKLOAD", "MAXIMIZE_PRIMARY_TRAINERS", "MINIMIZE_CONFLICTS"
}
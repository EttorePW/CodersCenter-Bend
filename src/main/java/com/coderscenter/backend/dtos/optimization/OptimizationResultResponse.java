package com.coderscenter.backend.dtos.optimization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationResultResponse {
    
    private String jobId;
    private Long scheduleId;
    private Boolean applied;
    private Integer changedSlots;
    private String message;
    private String summary;
}
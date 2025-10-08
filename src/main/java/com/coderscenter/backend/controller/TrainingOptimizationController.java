package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.optimization.OptimizationJobResponse;
import com.coderscenter.backend.dtos.optimization.OptimizationRequest;
import com.coderscenter.backend.dtos.optimization.OptimizationResultResponse;
import com.coderscenter.backend.services.TrainingOptimizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/api/training-optimization")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class TrainingOptimizationController {

    private final TrainingOptimizationService optimizationService;

    @PostMapping("/optimize/{scheduleId}")
    public ResponseEntity<OptimizationJobResponse> startOptimization(@PathVariable Long scheduleId) {
        try {
            OptimizationJobResponse job = optimizationService.startOptimization(scheduleId);
            return ResponseEntity.ok(job);
        } catch (IllegalArgumentException e) {
            log.warn("Schedule not found: {}", scheduleId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found: " + scheduleId);
        } catch (Exception e) {
            log.error("Error starting optimization for schedule {}: {}", scheduleId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to start optimization");
        }
    }

    @GetMapping("/status/{jobId}")
    public ResponseEntity<OptimizationJobResponse> getOptimizationStatus(@PathVariable String jobId) {
        try {
            OptimizationJobResponse status = optimizationService.getOptimizationStatus(jobId);
            return ResponseEntity.ok(status);
        } catch (IllegalArgumentException e) {
            log.warn("Job not found: {}", jobId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Optimization job not found: " + jobId);
        } catch (Exception e) {
            log.error("Error retrieving optimization status for job {}: {}", jobId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve optimization status");
        }
    }

    @PostMapping("/stop/{jobId}")
    public ResponseEntity<OptimizationJobResponse> stopOptimization(@PathVariable String jobId) {
        try {
            OptimizationJobResponse result = optimizationService.stopOptimization(jobId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            log.warn("Job not found: {}", jobId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Optimization job not found: " + jobId);
        } catch (Exception e) {
            log.error("Error stopping optimization for job {}: {}", jobId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to stop optimization");
        }
    }

    @PostMapping("/apply/{jobId}/{scheduleId}")
    public ResponseEntity<OptimizationResultResponse> applyOptimization(
            @PathVariable String jobId, 
            @PathVariable Long scheduleId) {
        try {
            OptimizationResultResponse result = optimizationService.applyOptimization(jobId, scheduleId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            log.warn("Job or schedule not found - Job: {}, Schedule: {}", jobId, scheduleId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error applying optimization for job {} and schedule {}: {}", jobId, scheduleId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to apply optimization");
        }
    }
    
    @GetMapping("/debug/jobs")
    public ResponseEntity<Map<String, String>> getAllJobs() {
        try {
            Map<String, String> jobs = optimizationService.getAllJobsDebugInfo();
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            log.error("Error retrieving debug job info: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve job info");
        }
    }
}
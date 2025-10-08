package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.optimization.OptimizationJobResponse;
import com.coderscenter.backend.dtos.optimization.OptimizationResultResponse;
import com.coderscenter.backend.entities.profile.Employee;
import com.coderscenter.backend.entities.schedule_management.Schedule;
import com.coderscenter.backend.entities.schedule_management.Slot;
import com.coderscenter.backend.optaplanner.constraints.ScheduleConstraintProvider;
import com.coderscenter.backend.optaplanner.domain.ScheduleSolution;
import com.coderscenter.backend.optaplanner.domain.SlotAssignment;
import com.coderscenter.backend.repositories.EmployeeRepository;
import com.coderscenter.backend.repositories.ScheduleRepository;
import com.coderscenter.backend.repositories.SlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingOptimizationService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final SlotRepository slotRepository;
    
    // In-memory job storage (in production, use database or cache)
    private final Map<String, OptimizationJobResponse> jobStorage = new ConcurrentHashMap<>();

    public OptimizationJobResponse startOptimization(Long scheduleId) {
        log.info("Starting real OptaPlanner optimization for schedule {}", scheduleId);
        
        // Verify schedule exists
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));
        
        // Create job
        String jobId = UUID.randomUUID().toString();
        OptimizationJobResponse job = OptimizationJobResponse.builder()
                .jobId(jobId)
                .status("running")
                .createdAt(LocalDateTime.now())
                .totalLessons(countLessonsInSchedule(schedule))
                .totalTrainers(countAvailableTrainers())
                .progress(0)
                .feasible(null)
                .message("OptaPlanner Optimierung gestartet...")
                .build();
        
        jobStorage.put(jobId, job);
        log.info("Created and stored new optimization job: {} for schedule: {}", jobId, scheduleId);
        log.info("Total jobs in storage after creation: {}", jobStorage.size());
        
        // Load schedule data synchronously before async processing to maintain transaction context
        Schedule loadedSchedule;
        List<Employee> availableEmployees;
        try {
            loadedSchedule = loadScheduleForOptimization(scheduleId);
            availableEmployees = loadEmployeesForOptimization();
            
            log.info("Successfully loaded schedule with {} weeks and {} employees for optimization", 
                    loadedSchedule.getWeeks().size(), availableEmployees.size());
                    
        } catch (Exception e) {
            log.error("Failed to load schedule data for optimization: {}", e.getMessage(), e);
            OptimizationJobResponse currentJob = jobStorage.get(jobId);
            if (currentJob != null) {
                currentJob.setStatus("failed");
                currentJob.setCompletedAt(LocalDateTime.now());
                currentJob.setFeasible(false);
                currentJob.setMessage("Fehler beim Laden der Schedule-Daten: " + e.getMessage());
                jobStorage.put(jobId, currentJob);
            }
            return job;
        }
        
        // Start real OptaPlanner optimization process with loaded data
        runOptaplannerOptimization(jobId, loadedSchedule, availableEmployees);
        
        return job;
    }

    public OptimizationJobResponse getOptimizationStatus(String jobId) {
        log.info("Looking for optimization job with ID: {}", jobId);
        log.info("Current jobs in storage: {}", jobStorage.keySet());
        
        OptimizationJobResponse job = jobStorage.get(jobId);
        if (job == null) {
            log.warn("Job not found in storage: {}. Available jobs: {}", jobId, jobStorage.keySet());
            throw new IllegalArgumentException("Job not found: " + jobId);
        }
        
        log.info("Found job: {} with status: {}", jobId, job.getStatus());
        return job;
    }
    
    public Map<String, String> getAllJobsDebugInfo() {
        log.info("Debug: Retrieving all jobs info. Total jobs: {}", jobStorage.size());
        return jobStorage.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().getStatus() + " (created: " + entry.getValue().getCreatedAt() + ")"
                ));
    }

    public OptimizationJobResponse stopOptimization(String jobId) {
        OptimizationJobResponse job = jobStorage.get(jobId);
        if (job == null) {
            throw new IllegalArgumentException("Job not found: " + jobId);
        }
        
        if ("running".equals(job.getStatus())) {
            job.setStatus("cancelled");
            job.setCompletedAt(LocalDateTime.now());
            job.setMessage("Optimierung wurde abgebrochen");
            jobStorage.put(jobId, job);
        }
        
        return job;
    }

    public OptimizationResultResponse applyOptimization(String jobId, Long scheduleId) {
        OptimizationJobResponse job = jobStorage.get(jobId);
        if (job == null) {
            throw new IllegalArgumentException("Job not found: " + jobId);
        }
        
        if (!"completed".equals(job.getStatus()) || !Boolean.TRUE.equals(job.getFeasible())) {
            throw new IllegalArgumentException("Cannot apply optimization: job not completed successfully");
        }
        
        if (job.getOptimizedSlotAssignments() == null || job.getOptimizedSlotAssignments().isEmpty()) {
            throw new IllegalArgumentException("No optimization results to apply");
        }
        
        log.info("Applying REAL OptaPlanner optimization results from job {} to schedule {}", jobId, scheduleId);
        
        // Apply the optimized assignments to actual slots
        int changedSlots = applyOptimizedAssignments(job.getOptimizedSlotAssignments());
        
        return OptimizationResultResponse.builder()
                .jobId(jobId)
                .scheduleId(scheduleId)
                .applied(true)
                .changedSlots(changedSlots)
                .message("OptaPlanner Optimierung erfolgreich angewendet")
                .summary(String.format(
                    "%d Trainerzuweisungen wurden durch OptaPlanner optimiert. " +
                    "Score: %s. Regelkonflikte behoben und Arbeitsbelastung ausgeglichen.", 
                    changedSlots, job.getScore()))
                .build();
    }
    
    private int applyOptimizedAssignments(List<SlotAssignment> optimizedAssignments) {
        int changedCount = 0;
        
        for (SlotAssignment assignment : optimizedAssignments) {
            Slot slot = assignment.getSlot();
            Employee newEmployee = assignment.getAssignedEmployee();
            
            // Check if assignment actually changed
            if (slot != null && !java.util.Objects.equals(slot.getEmployee(), newEmployee)) {
                Employee oldEmployee = slot.getEmployee();
                slot.setEmployee(newEmployee);
                slotRepository.save(slot);
                changedCount++;
                
                log.debug("Updated slot {} from {} to {}", 
                        slot.getSlotId(),
                        oldEmployee != null ? oldEmployee.getFirstName() + " " + oldEmployee.getLastName() : "unassigned",
                        newEmployee != null ? newEmployee.getFirstName() + " " + newEmployee.getLastName() : "unassigned");
            }
        }
        
        log.info("Applied {} optimized slot assignments to database", changedCount);
        return changedCount;
    }

    private void runOptaplannerOptimization(String jobId, Schedule schedule, List<Employee> availableEmployees) {
        CompletableFuture.runAsync(() -> {
            try {
                OptimizationJobResponse job = jobStorage.get(jobId);
                
                // Create OptaPlanner problem
                ScheduleSolution problem = createOptimizationProblem(schedule, availableEmployees);
                
                // Configure solver
                SolverConfig solverConfig = new SolverConfig()
                        .withSolutionClass(ScheduleSolution.class)
                        .withEntityClasses(SlotAssignment.class)
                        .withConstraintProviderClass(ScheduleConstraintProvider.class)
                        .withTerminationSpentLimit(Duration.ofSeconds(30)); // 30 second optimization
                
                SolverFactory<ScheduleSolution> solverFactory = SolverFactory.create(solverConfig);
                Solver<ScheduleSolution> solver = solverFactory.buildSolver();
                
                // Update job progress during solving
                updateJobProgress(jobId, 10, "Initialisiere OptaPlanner...");
                
                // Solve the problem
                ScheduleSolution solution = solver.solve(problem);
                
                // Update job progress
                updateJobProgress(jobId, 90, "Verarbeite Ergebnisse...");
                
                // Store optimized solution
                storeOptimizedSolution(jobId, solution);
                
                // Complete the job
                if (!"cancelled".equals(job.getStatus())) {
                    job.setStatus("completed");
                    job.setCompletedAt(LocalDateTime.now());
                    job.setFeasible(solution.getScore().isFeasible());
                    job.setScore(solution.getScore().toString());
                    job.setPrimaryTrainerAssignments((int) solution.getAssignedSlots());
                    job.setPrimaryTrainerPercentage(
                        solution.getTotalSlots() > 0 
                            ? (solution.getAssignedSlots() * 100.0) / solution.getTotalSlots()
                            : 0.0
                    );
                    job.setProgress(100);
                    job.setMessage("OptaPlanner Optimierung erfolgreich abgeschlossen");
                    job.setFullReport(generateOptimizationReport(solution));
                    jobStorage.put(jobId, job);
                    
                    log.info("OptaPlanner optimization completed for job {} with score: {}", jobId, solution.getScore());
                }
                
            } catch (Exception e) {
                log.error("OptaPlanner optimization failed for job: " + jobId, e);
                OptimizationJobResponse job = jobStorage.get(jobId);
                if (job != null) {
                    job.setStatus("failed");
                    job.setCompletedAt(LocalDateTime.now());
                    job.setFeasible(false);
                    job.setMessage("OptaPlanner Optimierung fehlgeschlagen: " + e.getMessage());
                    jobStorage.put(jobId, job);
                }
            }
        });
    }

    private ScheduleSolution createOptimizationProblem(Schedule schedule, List<Employee> availableEmployees) {
        // Get all slots from the schedule
        List<Slot> allSlots = schedule.getWeeks().stream()
                .flatMap(week -> week.getDays().stream())
                .flatMap(day -> day.getSlots().stream())
                .collect(Collectors.toList());
        
        // Create a map of employee IDs to fully initialized employees for consistent references
        Map<Long, Employee> employeeMap = availableEmployees.stream()
                .collect(Collectors.toMap(Employee::getId, emp -> emp));
        
        // Create slot assignments with consistent employee references
        List<SlotAssignment> slotAssignments = allSlots.stream()
                .map(slot -> {
                    // Use the fully initialized employee from availableEmployees instead of slot.getEmployee()
                    Employee currentEmployee = slot.getEmployee();
                    Employee initializedEmployee = null;
                    
                    if (currentEmployee != null && employeeMap.containsKey(currentEmployee.getId())) {
                        initializedEmployee = employeeMap.get(currentEmployee.getId());
                        log.debug("Mapping slot {} from employee {} to initialized employee {}", 
                                slot.getSlotId(), currentEmployee.getId(), initializedEmployee.getId());
                    }
                    
                    return new SlotAssignment(slot, initializedEmployee);
                })
                .collect(Collectors.toList());
        
        // Filter employees who can teach at least one subject in this schedule
        List<Employee> relevantEmployees = availableEmployees.stream()
                .filter(emp -> emp.getSubjects() != null && !emp.getSubjects().isEmpty())
                .collect(Collectors.toList());
        
        log.info("Created optimization problem with {} slots and {} available employees (mapped from {} total)", 
                 slotAssignments.size(), relevantEmployees.size(), availableEmployees.size());
        
        return new ScheduleSolution(relevantEmployees, slotAssignments);
    }
    
    private void updateJobProgress(String jobId, int progress, String message) {
        OptimizationJobResponse job = jobStorage.get(jobId);
        if (job != null && !"cancelled".equals(job.getStatus())) {
            job.setProgress(progress);
            job.setMessage(message);
            jobStorage.put(jobId, job);
        }
    }
    
    private void storeOptimizedSolution(String jobId, ScheduleSolution solution) {
        // Store the optimized solution in job storage for later application
        // In a real implementation, you might serialize this to database
        OptimizationJobResponse job = jobStorage.get(jobId);
        if (job != null) {
            // Store solution data in job for later retrieval
            job.setOptimizedSlotAssignments(solution.getSlotAssignmentList());
            jobStorage.put(jobId, job);
        }
    }
    
    private String generateOptimizationReport(ScheduleSolution solution) {
        HardSoftScore score = solution.getScore();
        long assignedSlots = solution.getAssignedSlots();
        int totalSlots = solution.getTotalSlots();
        long usedEmployees = solution.getUsedEmployees();
        
        return String.format(
            "OPTAPLANNER OPTIMIERUNG ABGESCHLOSSEN\n\n" +
            "Bewertung: %s\n" +
            "- Hard Score: %d (Regelverstöße)\n" +
            "- Soft Score: %d (Optimierungsqualität)\n\n" +
            "Trainerzuweisungen: %d von %d (%,.1f%%)\n" +
            "Verwendete Trainer: %d von %d\n\n" +
            "OPTIMIERUNGSKRITERIEN:\n" +
            "✓ Fachkompetenz beachtet\n" +
            "✓ Trainerverfügbarkeit geprüft\n" +
            "✓ Arbeitsbelastung ausgeglichen\n" +
            "✓ Haupt-Trainer bevorzugt\n" +
            "✓ Terminüberschneidungen vermieden\n\n" +
            "STATUS: %s",
            score.toString(),
            score.hardScore(),
            score.softScore(),
            assignedSlots,
            totalSlots,
            totalSlots > 0 ? (assignedSlots * 100.0) / totalSlots : 0.0,
            usedEmployees,
            solution.getTotalEmployees(),
            score.isFeasible() ? "Optimale Lösung gefunden" : "Keine optimale Lösung möglich"
        );
    }
    
    private Integer countAvailableTrainers() {
        return (int) employeeRepository.count();
    }
    
    /**
     * Load schedule data within transaction to avoid LazyInitializationException
     */
    @Transactional(readOnly = true)
    public Schedule loadScheduleForOptimization(Long scheduleId) {
        // Load schedule with weeks
        Schedule schedule = scheduleRepository.findByIdWithWeeks(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));
        
        // Manually initialize all nested collections while in transaction
        schedule.getWeeks().forEach(week -> {
            week.getDays().forEach(day -> {
                day.getSlots().forEach(slot -> {
                    // Initialize subject and employee
                    if (slot.getSubject() != null) {
                        slot.getSubject().getName(); // Force initialization
                    }
                    if (slot.getEmployee() != null) {
                        Employee employee = slot.getEmployee();
                        
                        // Initialize the employee proxy itself
                        Hibernate.initialize(employee);
                        
                        // Initialize all employee collections
                        if (employee.getSubjects() != null) {
                            Hibernate.initialize(employee.getSubjects());
                            employee.getSubjects().size(); // Force initialization
                        }
                        if (employee.getWorkDays() != null) {
                            Hibernate.initialize(employee.getWorkDays());
                            employee.getWorkDays().size(); // Force initialization
                        }
                        if (employee.getHolidays() != null) {
                            Hibernate.initialize(employee.getHolidays());
                            employee.getHolidays().size(); // Force initialization
                        }
                        if (employee.getUnavailableDates() != null) {
                            Hibernate.initialize(employee.getUnavailableDates());
                            employee.getUnavailableDates().size(); // Force initialization
                        }
                        if (employee.getCourses() != null) {
                            Hibernate.initialize(employee.getCourses());
                            employee.getCourses().size(); // Force initialization
                        }
                        
                        log.debug("Initialized employee {} in slot {} with workDays: {}", 
                                employee.getId(), slot.getSlotId(), employee.getWorkDays());
                    }
                });
            });
        });
        
        return schedule;
    }
    
    /**
     * Load employees with all collections for OptaPlanner optimization
     * Uses explicit Hibernate initialization to prevent LazyInitializationException
     */
    @Transactional(readOnly = true)
    public List<Employee> loadEmployeesForOptimization() {
        List<Employee> employees = employeeRepository.findAll();
        
        log.info("Loading {} employees for optimization with full collection initialization", employees.size());
        
        // Explicitly initialize all collections using Hibernate.initialize()
        employees.forEach(emp -> {
            // Initialize the employee proxy itself
            Hibernate.initialize(emp);
            
            // Initialize subjects collection
            if (emp.getSubjects() != null) {
                Hibernate.initialize(emp.getSubjects());
                emp.getSubjects().size(); // Force access
                log.debug("Employee {} has {} subjects", emp.getId(), emp.getSubjects().size());
            }
            
            // Initialize workDays collection  
            if (emp.getWorkDays() != null) {
                Hibernate.initialize(emp.getWorkDays());
                emp.getWorkDays().size(); // Force access
                log.debug("Employee {} has {} work days: {}", emp.getId(), emp.getWorkDays().size(), emp.getWorkDays());
            }
            
            // Initialize holidays collection
            if (emp.getHolidays() != null) {
                Hibernate.initialize(emp.getHolidays());
                emp.getHolidays().size(); // Force access
                log.debug("Employee {} has {} holidays", emp.getId(), emp.getHolidays().size());
            }
            
            // Initialize unavailableDates collection
            if (emp.getUnavailableDates() != null) {
                Hibernate.initialize(emp.getUnavailableDates());
                emp.getUnavailableDates().size(); // Force access
                log.debug("Employee {} has {} unavailable dates", emp.getId(), emp.getUnavailableDates().size());
            }
            
            // Initialize courses collection
            if (emp.getCourses() != null) {
                Hibernate.initialize(emp.getCourses());
                emp.getCourses().size(); // Force access
                log.debug("Employee {} has {} courses", emp.getId(), emp.getCourses().size());
            }
        });
        
        log.info("Successfully initialized all collections for {} employees", employees.size());
        return employees;
    }

    private Integer countLessonsInSchedule(Schedule schedule) {
        return schedule.getWeeks().stream()
                .mapToInt(week -> week.getDays().stream()
                        .mapToInt(day -> day.getSlots().size())
                        .sum())
                .sum();
    }

    private Integer countTrainersInSchedule(Schedule schedule) {
        return (int) schedule.getWeeks().stream()
                .flatMap(week -> week.getDays().stream())
                .flatMap(day -> day.getSlots().stream())
                .map(slot -> slot.getEmployee())
                .filter(employee -> employee != null)
                .distinct()
                .count();
    }
}
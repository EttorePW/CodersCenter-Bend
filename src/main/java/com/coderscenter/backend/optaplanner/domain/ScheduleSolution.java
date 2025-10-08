package com.coderscenter.backend.optaplanner.domain;

import com.coderscenter.backend.entities.profile.Employee;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

/**
 * OptaPlanner Planning Solution for Schedule Optimization
 * Contains all slots to be assigned and available employees
 */
@PlanningSolution
public class ScheduleSolution {
    
    private List<Employee> employeeList; // Available employees (fact)
    private List<SlotAssignment> slotAssignmentList; // Slots to be assigned (planning entities)
    private HardSoftScore score; // Calculated score
    
    // Default constructor required by OptaPlanner
    public ScheduleSolution() {}
    
    public ScheduleSolution(List<Employee> employeeList, List<SlotAssignment> slotAssignmentList) {
        this.employeeList = employeeList;
        this.slotAssignmentList = slotAssignmentList;
    }
    
    // Problem facts - these don't change during optimization
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "employeeRange")
    public List<Employee> getEmployeeList() {
        return employeeList;
    }
    
    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
    
    // Planning entities - these will be optimized
    @PlanningEntityCollectionProperty
    public List<SlotAssignment> getSlotAssignmentList() {
        return slotAssignmentList;
    }
    
    public void setSlotAssignmentList(List<SlotAssignment> slotAssignmentList) {
        this.slotAssignmentList = slotAssignmentList;
    }
    
    // Score - calculated by constraint provider
    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }
    
    public void setScore(HardSoftScore score) {
        this.score = score;
    }
    
    // Utility methods for reporting
    public int getTotalSlots() {
        return slotAssignmentList != null ? slotAssignmentList.size() : 0;
    }
    
    public long getAssignedSlots() {
        if (slotAssignmentList == null) return 0;
        return slotAssignmentList.stream()
                .mapToLong(sa -> sa.getAssignedEmployee() != null ? 1 : 0)
                .sum();
    }
    
    public int getTotalEmployees() {
        return employeeList != null ? employeeList.size() : 0;
    }
    
    public long getUsedEmployees() {
        if (slotAssignmentList == null) return 0;
        return slotAssignmentList.stream()
                .filter(sa -> sa.getAssignedEmployee() != null)
                .map(sa -> sa.getAssignedEmployee().getId())
                .distinct()
                .count();
    }
    
    @Override
    public String toString() {
        return "ScheduleSolution{" +
                "employees=" + getTotalEmployees() +
                ", slots=" + getTotalSlots() +
                ", assigned=" + getAssignedSlots() +
                ", score=" + score +
                '}';
    }
}
package com.coderscenter.backend.optaplanner.domain;

import com.coderscenter.backend.entities.profile.Employee;
import com.coderscenter.backend.entities.schedule_management.Slot;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * OptaPlanner Planning Entity for slot assignments
 * Each slot can be assigned to an employee (trainer)
 */
@PlanningEntity
public class SlotAssignment {
    
    private Slot slot; // The original slot from database
    private Employee assignedEmployee; // Planning variable - to be optimized
    
    // Default constructor required by OptaPlanner
    public SlotAssignment() {}
    
    public SlotAssignment(Slot slot, Employee assignedEmployee) {
        this.slot = slot;
        this.assignedEmployee = assignedEmployee;
    }
    
    // OptaPlanner ID for unique identification
    @PlanningId
    public Long getId() {
        return slot != null ? slot.getSlotId() : null;
    }
    
    // Getters and Setters
    public Slot getSlot() {
        return slot;
    }
    
    public void setSlot(Slot slot) {
        this.slot = slot;
    }
    
    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }
    
    public void setAssignedEmployee(Employee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }
    
    // Utility methods for constraints
    public boolean canEmployeeTeachSubject() {
        if (assignedEmployee == null || slot == null || slot.getSubject() == null) {
            return false;
        }
        
        try {
            return assignedEmployee.getSubjects().contains(slot.getSubject());
        } catch (org.hibernate.LazyInitializationException e) {
            // Defensive handling: if subjects collection is not initialized, assume cannot teach
            // This should not happen if loadEmployeesForOptimization() works correctly
            System.err.println("WARNING: LazyInitializationException for employee " + 
                              assignedEmployee.getId() + " subjects - assuming cannot teach. Error: " + e.getMessage());
            return false; // Default to cannot teach to maintain constraint safety
        }
    }
    
    public boolean isEmployeeAvailableOnDay() {
        if (assignedEmployee == null || slot == null || slot.getDay() == null) {
            return false;
        }
        
        try {
            return assignedEmployee.isAvailableOn(slot.getStartDate().toLocalDate());
        } catch (org.hibernate.LazyInitializationException e) {
            // Defensive handling: if collections are not initialized, assume available
            // This should not happen if loadEmployeesForOptimization() works correctly
            System.err.println("WARNING: LazyInitializationException for employee " + 
                              assignedEmployee.getId() + " - assuming available. Error: " + e.getMessage());
            return true; // Default to available rather than failing optimization
        }
    }
    
    @Override
    public String toString() {
        return "SlotAssignment{" +
                "slotId=" + (slot != null ? slot.getSlotId() : "null") +
                ", employeeName=" + (assignedEmployee != null ? assignedEmployee.getFirstName() + " " + assignedEmployee.getLastName() : "null") +
                '}';
    }
}
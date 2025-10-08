package com.coderscenter.backend.optaplanner.constraints;

import com.coderscenter.backend.optaplanner.domain.SlotAssignment;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

/**
 * OptaPlanner Constraint Provider für Schedule Optimization
 * Bereinigt und verbessert: Gleichverteilung der Trainer pro Fach und Tag
 */
public class ScheduleConstraintProvider implements ConstraintProvider {

    private static final int IDEAL_WORKLOAD_PER_TRAINER = 4; // Beispielwert, dynamisch berechnbar

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                employeeMustTeachSubject(constraintFactory),
                employeeMustBeAvailableOnDay(constraintFactory),
                employeeCannotBeInTwoPlacesAtOnce(constraintFactory),

                // Soft constraints
                balanceWorkloadDynamic(constraintFactory),
                balanceTrainerPerSubjectAndDay(constraintFactory),
                preferMainTrainerForGroup(constraintFactory),
                minimizeEmployeeSwitching(constraintFactory)
        };
    }

    // ===================== Hard Constraints =====================

    Constraint employeeMustTeachSubject(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(SlotAssignment.class)
                .filter(a -> a.getAssignedEmployee() != null)
                .filter(a -> !a.canEmployeeTeachSubject())
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Employee must teach subject");
    }

    Constraint employeeMustBeAvailableOnDay(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(SlotAssignment.class)
                .filter(a -> a.getAssignedEmployee() != null)
                .filter(a -> !a.isEmployeeAvailableOnDay())
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Employee must be available on day");
    }

    Constraint employeeCannotBeInTwoPlacesAtOnce(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(SlotAssignment.class,
                        Joiners.equal(SlotAssignment::getAssignedEmployee),
                        Joiners.overlapping(a -> a.getSlot().getStartDate(), a -> a.getSlot().getEndDate()))
                .filter((a1, a2) -> a1.getAssignedEmployee() != null)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Employee cannot be in two places at once");
    }

    // ===================== Soft Constraints =====================

    /**
     * Gleichmäßige Verteilung der Trainer pro Tag und Fach
     */
    Constraint balanceTrainerPerSubjectAndDay(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(SlotAssignment.class)
                .filter(a -> a.getAssignedEmployee() != null)
                .groupBy(
                        a -> a.getAssignedEmployee(),
                        a -> a.getSlot().getSubject(),
                        a -> a.getSlot().getDay().getLabel(),
                        ConstraintCollectors.count()
                )
                .penalize(HardSoftScore.ofSoft(1),
                        (employee, subject, day, count) -> Math.abs(count - 1))
                .asConstraint("Balance trainer per subject and day");
    }

    /**
     * Gleichmäßige Verteilung der Gesamtworkload pro Trainer
     */
    Constraint balanceWorkloadDynamic(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(SlotAssignment.class)
                .filter(a -> a.getAssignedEmployee() != null)
                .groupBy(SlotAssignment::getAssignedEmployee, ConstraintCollectors.count())
                .penalize(HardSoftScore.ofSoft(1),
                        (employee, count) -> Math.abs(count - IDEAL_WORKLOAD_PER_TRAINER))
                .asConstraint("Balance workload dynamically");
    }

    /**
     * Bevorzugung des Haupttrainers für die Gruppe
     */
    Constraint preferMainTrainerForGroup(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(SlotAssignment.class)
                .filter(a -> a.getAssignedEmployee() != null)
                .filter(this::isMainTrainerForGroupSubject)
                .reward(HardSoftScore.ofSoft(10))
                .asConstraint("Prefer main trainer for group");
    }

    /**
     * Minimierung von Trainerwechseln innerhalb eines Tages/Fachs
     */
    Constraint minimizeEmployeeSwitching(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(SlotAssignment.class,
                        Joiners.equal(a -> a.getSlot().getDay()),
                        Joiners.equal(a -> a.getSlot().getSubject()))
                .filter((a1, a2) -> a1.getAssignedEmployee() != null &&
                        a2.getAssignedEmployee() != null &&
                        !a1.getAssignedEmployee().equals(a2.getAssignedEmployee()))
                .penalize(HardSoftScore.ofSoft(1))
                .asConstraint("Minimize employee switching");
    }

    // ===================== Helper =====================

    private boolean isMainTrainerForGroupSubject(SlotAssignment assignment) {
        if (assignment.getAssignedEmployee() == null ||
                assignment.getSlot() == null ||
                assignment.getSlot().getSubject() == null ||
                assignment.getSlot().getDay() == null ||
                assignment.getSlot().getDay().getWeek() == null ||
                assignment.getSlot().getDay().getWeek().getSchedule() == null) {
            return false;
        }
        try {
            return assignment.getAssignedEmployee().getCourses().stream()
                    .anyMatch(course ->
                            course.getGroup().equals(assignment.getSlot().getDay().getWeek().getSchedule().getGroup()) &&
                                    course.getSubject().equals(assignment.getSlot().getSubject()));
        } catch (org.hibernate.LazyInitializationException e) {
            System.err.println("WARNING: LazyInitializationException for employee " +
                    assignment.getAssignedEmployee().getId() + " courses - assuming not main trainer. Error: " + e.getMessage());
            return false;
        }
    }
}

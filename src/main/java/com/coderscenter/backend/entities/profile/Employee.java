package com.coderscenter.backend.entities.profile;

import com.coderscenter.backend.entities.group_management.Subject;
import com.coderscenter.backend.entities.pk.course_subject.Course_Subject_Employee_in;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.coderscenter.backend.enums.DayLabel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @Column(unique = true, nullable = false)
    private String svn;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(nullable = false)
    private Double salary;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "employee")
    private List<Course_Subject_Employee_in> courses = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "employee_subject",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "subject_id"})
    )
    private List<Subject> subjects = new ArrayList<>();

    // Wochentage an denen der Trainer arbeitet (z.B: MONTAG, DIENSTAG, FREITAG)
    @ElementCollection(targetClass = DayLabel.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "employee_workdays", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "work_day")
    private Set<DayLabel> workDays = new HashSet<>();

    // Geplante Urlaubstage
    @ElementCollection
    @CollectionTable(name = "employee_holidays", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "holiday_date")
    private Set<LocalDate> holidays = new HashSet<>();

    // Spezifische Tage an denen nicht verfügbar (Krankheit, persönliche Angelegenheiten, etc.)
    @ElementCollection
    @CollectionTable(name = "employee_unavailable_dates", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "unavailable_date")
    private Set<LocalDate> unavailableDates = new HashSet<>();

    /**
     * Hilfsmethode um Verfügbarkeit zu prüfen
     * @param date Das zu prüfende Datum
     * @return true wenn der Trainer an diesem Tag verfügbar ist
     */
    public boolean isAvailableOn(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        DayLabel dayLabel = convertDayOfWeekToDayLabel(dayOfWeek);
        
        // Arbeitet nicht an diesem Wochentag
        if (!workDays.contains(dayLabel)) {
            return false;
        }
        
        // Ist im Urlaub
        if (holidays.contains(date)) {
            return false;
        }
        
        // Aus anderen Gründen nicht verfügbar
        if (unavailableDates.contains(date)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Konvertiert Java DayOfWeek zu deutschem DayLabel
     */
    private DayLabel convertDayOfWeekToDayLabel(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DayLabel.MONTAG;
            case TUESDAY -> DayLabel.DIENSTAG;
            case WEDNESDAY -> DayLabel.MITTWOCH;
            case THURSDAY -> DayLabel.DONNERSTAG;
            case FRIDAY -> DayLabel.FREITAG;
            case SATURDAY -> DayLabel.SAMSTAG;
            case SUNDAY -> DayLabel.SONNTAG;
            default -> throw new IllegalArgumentException("Unsupported day: " + dayOfWeek);
        };
    }
}

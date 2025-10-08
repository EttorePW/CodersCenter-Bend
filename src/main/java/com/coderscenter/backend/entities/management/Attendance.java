package com.coderscenter.backend.entities.management;

import com.coderscenter.backend.entities.profile.Student;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "Attendance")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @Column(nullable = false)
    private boolean isPresent = false;

    private String reasonForAbsence;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="student_id")
    Student student;
}

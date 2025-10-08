package com.coderscenter.backend.entities.schedule_management;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Table(name = "Week")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Week {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weekId;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime weekStartDate;

    @ManyToOne
    @JoinColumn(name="schedule_id")
    private Schedule schedule;

    @OneToMany(mappedBy = "week", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Day> days;


}

package com.coderscenter.backend.entities.schedule_management;

import com.coderscenter.backend.enums.DayLabel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Table(name = "Days")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dayId;

    @Enumerated(EnumType.STRING)
    private DayLabel label;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dayDate;

    @ManyToOne
    @JoinColumn(name="week_id")
    private Week week;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Slot> slots;
}

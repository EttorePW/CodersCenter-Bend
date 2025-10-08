package com.coderscenter.backend.entities.management;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Replacements")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Replacements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replacementId;
    @Column(nullable = false)
    private String employeeId;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private String slotId;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private String reason;

}

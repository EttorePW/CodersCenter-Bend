package com.coderscenter.backend.entities.profile;

import com.coderscenter.backend.enums.PermissionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "permissions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionType name;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    private String description;

}

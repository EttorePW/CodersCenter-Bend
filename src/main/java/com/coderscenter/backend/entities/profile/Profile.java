package com.coderscenter.backend.entities.profile;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "profiles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String phone;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id", unique = true)
    private ProfileImage profileImage;
}

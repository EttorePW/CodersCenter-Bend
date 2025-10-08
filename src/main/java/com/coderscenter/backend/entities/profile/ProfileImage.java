package com.coderscenter.backend.entities.profile;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile_image")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_image_id")
    private Long id;

    @Column(nullable = false)
    private String profileImageUrl;
}

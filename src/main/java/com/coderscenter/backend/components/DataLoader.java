package com.coderscenter.backend.components;

import com.coderscenter.backend.entities.profile.*;
import com.coderscenter.backend.enums.PermissionType;
import com.coderscenter.backend.enums.Role;
import com.coderscenter.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByRole(Role.ADMIN).isEmpty()) {
            // Permission in die DB
            Permission permissionR = Permission.builder()
                    .name(PermissionType.REGISTER)
                    .description("User can Register other user")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .build();
            permissionRepository.save(permissionR);

            Permission permissionS = Permission.builder()
                    .name(PermissionType.SCHEDULE)
                    .description("User can manage the Schedule")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .build();
            permissionRepository.save(permissionS);

            // Admin in die DB
            List<Permission> permissions = new ArrayList<>();

            ProfileImage profileImageEmre = new ProfileImage();
            profileImageEmre.setProfileImageUrl("foto_quadrat.jpg");

            Profile profileEmre = new Profile();
            profileEmre.setFirstName("Emrehan");
            profileEmre.setLastName("Imamoglu");
            profileEmre.setPhone("06769430015");
            profileEmre.setBirthDate(LocalDate.of(1993,6,20));
            profileEmre.setProfileImage(profileImageEmre);

            User emre = User.builder()
                    .username("emre")
                    .email("emre@admin.com")
                    .enabled(true)
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .permissions(permissions)
                    .profile(profileEmre)
                    .build();

            ProfileImage profileImageJunior = new ProfileImage();
            profileImageJunior.setProfileImageUrl("junior.jpg");

            Profile profileJunior = new Profile();
            profileJunior.setFirstName("Ettore Junior");
            profileJunior.setLastName("Pesendorfer Wagner");
            profileJunior.setPhone("067761265572");
            profileJunior.setBirthDate(LocalDate.of(1990,9,22));
            profileJunior.setProfileImage(profileImageJunior);

            User junior = User.builder()
                    .username("junior")
                    .email("junior@admin.com")
                    .enabled(true)
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .permissions(permissions)
                    .profile(profileJunior)
                    .build();

            userRepository.save(emre);
            userRepository.save(junior);


        }
    }
}

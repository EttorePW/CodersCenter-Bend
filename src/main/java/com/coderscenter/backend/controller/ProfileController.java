package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.profile.RequestProfileDTO;
import com.coderscenter.backend.dtos.profile.ResponseProfileDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.services.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Get all profiles
     * @return list of {@link ResponseProfileDTO}
     */
    @GetMapping
    public ResponseEntity<List<ResponseProfileDTO>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    /**
     * Get a profile by the associated user ID.
     *
     * @param userId ID of the user
     * @return {@link ResponseProfileDTO} with profile details
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    /**
     * Update a profile by ID with new values.
     *
     * @param profileId         ID of the profile to update
     * @param requestProfileDTO DTO with new values
     * @return {@link ResponseProfileDTO} with updated profile details
     */
    @PutMapping("/{profileId}")
    public ResponseEntity<ResponseProfileDTO> updateProfile(
            @PathVariable Long profileId,
            @Valid @RequestBody RequestProfileDTO requestProfileDTO) {
        return ResponseEntity.ok(profileService.updateProfile(profileId, requestProfileDTO));
    }
}

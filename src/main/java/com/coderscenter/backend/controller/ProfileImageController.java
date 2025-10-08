package com.coderscenter.backend.controller;

import com.coderscenter.backend.services.ProfileImageService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/profile-image")
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    /**
     * Upload a profile image for a given profile.
     *
     * @param profileId ID of the profile
     * @param file image file to upload
     * @return filename of uploaded image
     * @throws IOException if file saving fails
     */
    @PostMapping("/{profileId}/upload")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable Long profileId,
            @RequestParam("file") MultipartFile file) throws IOException {

        String filename = profileImageService.uploadImage(profileId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(filename);
    }

    /**
     * Delete profile image for a given profile.
     *
     * @param profileId ID of the profile
     * @return success message
     * @throws IOException if deletion fails
     */
    @DeleteMapping("/{profileId}")
    public ResponseEntity<String> deleteProfileImage(@PathVariable Long profileId) throws IOException {
        profileImageService.deleteProfileImage(profileId);
        return ResponseEntity.ok("Profilbild erfolgreich gelöscht");
    }
}

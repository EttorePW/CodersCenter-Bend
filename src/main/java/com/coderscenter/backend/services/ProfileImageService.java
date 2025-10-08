package com.coderscenter.backend.services;

import com.coderscenter.backend.entities.profile.Profile;
import com.coderscenter.backend.entities.profile.ProfileImage;
import com.coderscenter.backend.exceptions.InvalidFileTypeException;
import com.coderscenter.backend.exceptions.ProfileNotFoundException;
import com.coderscenter.backend.repositories.ProfileImageRepository;
import com.coderscenter.backend.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final ProfileRepository profileRepository;

    private final String uploadDir = "uploads/"; // directory for storing images
    private final List<String> allowedMimeTypes = List.of("image/jpeg", "image/png", "image/gif");

    /**
     * Upload an image for a profile
     * Stores the image in the filesystem and saves the URL in the database
     *
     * @param profileId ID of the profile
     * @param file      image file to upload
     * @return the URL of the uploaded image
     * @throws IOException              if file upload fails
     * @throws ProfileNotFoundException if profile with given ID does not exist
     * @throws InvalidFileTypeException if the file type is not allowed
     */
    public String uploadImage(Long profileId, MultipartFile file) throws IOException {
        validateMimeType(file);
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profil mit der ID " + profileId + " nicht gefunden"));

        // Create directory if not exists
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Generate unique filename
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir, filename);

        // Save file to filesystem
        Files.write(filepath, file.getBytes());

        // Remove old image if exists
        if (profile.getProfileImage() != null) {
            deleteProfileImage(profileId);
        }

        // Save new image record in DB
        ProfileImage profileImage = ProfileImage.builder()
                .profileImageUrl(filename)
                .build();

        profileImageRepository.save(profileImage);

        profile.setProfileImage(profileImage);
        profileRepository.save(profile);

        return filename; // return the URL as needed
    }

    /**
     * Delete profile image both from filesystem and database
     *
     * @param profileId ID of the profile
     * @throws IOException              if file deletion fails
     * @throws ProfileNotFoundException if profile with given ID does not exist
     */
    public void deleteProfileImage(Long profileId) throws IOException {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profil mit der ID " + profileId + " nicht gefunden"));

        if (profile.getProfileImage() != null) {
            ProfileImage profileImage = profile.getProfileImage();
            Path path = Paths.get(uploadDir, profileImage.getProfileImageUrl());

            if (Files.exists(path)) {
                Files.delete(path);
            }

            // Set profile image to null first
            profile.setProfileImage(null);
            // Save profile without image reference
            profileRepository.save(profile);

            // Then delete the image record from DB
            profileImageRepository.delete(profileImage);
        }
    }

    /**
     * Validate the MIME type of the uploaded file
     *
     * @param file the uploaded file
     * @throws InvalidFileTypeException if the file type is not allowed
     */
    private void validateMimeType(MultipartFile file) {
        String mimeType = file.getContentType();
        if (mimeType == null || !allowedMimeTypes.contains(mimeType)) {
            throw new InvalidFileTypeException("Ungültiger Dateityp: " + mimeType);
        }
    }
}

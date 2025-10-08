package com.coderscenter.backend.services;

import com.coderscenter.backend.entities.profile.Profile;
import com.coderscenter.backend.entities.profile.ProfileImage;
import com.coderscenter.backend.exceptions.InvalidFileTypeException;
import com.coderscenter.backend.exceptions.ProfileNotFoundException;
import com.coderscenter.backend.repositories.ProfileImageRepository;
import com.coderscenter.backend.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final ProfileRepository profileRepository;
    private final CloudinaryImageService cloudinaryImageService;

    private final String uploadDir = "uploads/"; // directory for storing images (fallback)
    private final List<String> allowedMimeTypes = List.of("image/jpeg", "image/png", "image/gif", "image/webp");

    /**
     * Upload an image for a profile
     * Uses Cloudinary if available, otherwise falls back to local filesystem
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

        // Remove old image if exists
        if (profile.getProfileImage() != null) {
            deleteProfileImage(profileId);
        }

        String imageUrl;
        
        // Try Cloudinary first (persistent storage)
        if (cloudinaryImageService.isCloudinaryAvailable()) {
            log.info("Uploading image to Cloudinary for profile: {}", profileId);
            imageUrl = cloudinaryImageService.uploadProfileImage(file, profileId.toString());
            
            if (imageUrl != null) {
                log.info("Image uploaded successfully to Cloudinary: {}", imageUrl);
            } else {
                log.warn("Cloudinary upload failed, falling back to local storage");
                imageUrl = uploadToLocalStorage(file);
            }
        } else {
            // Fallback to local storage
            log.info("Using local storage for profile image: {}", profileId);
            imageUrl = uploadToLocalStorage(file);
        }

        // Save image record in database
        ProfileImage profileImage = ProfileImage.builder()
                .profileImageUrl(imageUrl)
                .build();

        profileImageRepository.save(profileImage);
        profile.setProfileImage(profileImage);
        profileRepository.save(profile);

        return imageUrl;
    }

    /**
     * Upload image to local storage (fallback method)
     */
    private String uploadToLocalStorage(MultipartFile file) throws IOException {
        // Create directory if not exists
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Generate unique filename
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir, filename);

        // Save file to filesystem
        Files.write(filepath, file.getBytes());
        
        return filename; // return local filename
    }

    /**
     * Delete profile image from Cloudinary/filesystem and database
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
            String imageUrl = profileImage.getProfileImageUrl();
            
            // Try to delete from Cloudinary first
            if (cloudinaryImageService.isCloudinaryAvailable() && imageUrl.startsWith("https://")) {
                log.info("Deleting image from Cloudinary: {}", imageUrl);
                String publicId = cloudinaryImageService.extractPublicIdFromUrl(imageUrl);
                if (publicId != null) {
                    boolean deleted = cloudinaryImageService.deleteImage(publicId);
                    if (deleted) {
                        log.info("Image deleted successfully from Cloudinary");
                    } else {
                        log.warn("Failed to delete image from Cloudinary: {}", imageUrl);
                    }
                }
            } else {
                // Delete local file
                Path path = Paths.get(uploadDir, imageUrl);
                if (Files.exists(path)) {
                    Files.delete(path);
                    log.info("Local image file deleted: {}", path);
                }
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
     * Get profile image URL with specific size (only works with Cloudinary)
     * @param profileId Profile ID
     * @param width Target width
     * @param height Target height
     * @return Resized image URL or original URL
     */
    public String getProfileImageUrl(Long profileId, int width, int height) {
        Profile profile = profileRepository.findById(profileId).orElse(null);
        if (profile == null || profile.getProfileImage() == null) {
            return null;
        }
        
        String originalUrl = profile.getProfileImage().getProfileImageUrl();
        
        // If Cloudinary URL, return resized version
        if (cloudinaryImageService.isCloudinaryAvailable() && originalUrl.startsWith("https://")) {
            return cloudinaryImageService.getResizedImageUrl(originalUrl, width, height);
        }
        
        // Return original URL for local images
        return originalUrl;
    }
    
    /**
     * Get profile image thumbnail (150x150)
     */
    public String getProfileImageThumbnail(Long profileId) {
        return getProfileImageUrl(profileId, 150, 150);
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
            throw new InvalidFileTypeException("Ungültiger Dateityp: " + mimeType + ". Erlaubte Typen: " + allowedMimeTypes);
        }
    }
}

package com.coderscenter.backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Cloudinary Image Service
 * Alternative to local file storage for Render deployment
 */
@Service
@Slf4j
public class CloudinaryImageService {

    private final Cloudinary cloudinary;
    
    @Value("${cloudinary.enabled:false}")
    private boolean cloudinaryEnabled;

    public CloudinaryImageService(
            @Value("${cloudinary.cloud-name:}") String cloudName,
            @Value("${cloudinary.api-key:}") String apiKey,
            @Value("${cloudinary.api-secret:}") String apiSecret) {
        
        if (cloudName.isEmpty() || apiKey.isEmpty() || apiSecret.isEmpty()) {
            log.warn("Cloudinary credentials not configured. Using local storage fallback.");
            this.cloudinary = null;
        } else {
            this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
            ));
            log.info("Cloudinary initialized successfully");
        }
    }

    /**
     * Upload image to Cloudinary
     * @param file MultipartFile to upload
     * @param folder Cloudinary folder (e.g. "profile-images")
     * @return Cloudinary URL or null if failed
     */
    public String uploadImage(MultipartFile file, String folder) {
        if (!cloudinaryEnabled || cloudinary == null) {
            log.warn("Cloudinary not enabled or configured");
            return null;
        }

        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image",
                "format", "webp", // Convert to WebP for better performance
                "quality", "auto:good", // Automatic quality optimization
                "fetch_format", "auto"
            );

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(), 
                uploadParams
            );

            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("Image uploaded successfully to Cloudinary: {}", imageUrl);
            return imageUrl;

        } catch (IOException e) {
            log.error("Error uploading image to Cloudinary: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Upload profile image
     */
    public String uploadProfileImage(MultipartFile file, String userId) {
        return uploadImage(file, "coders-center/profiles/" + userId);
    }

    /**
     * Delete image from Cloudinary
     * @param publicId Cloudinary public ID (extracted from URL)
     * @return true if successful
     */
    public boolean deleteImage(String publicId) {
        if (!cloudinaryEnabled || cloudinary == null) {
            return false;
        }

        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");
            boolean success = "ok".equals(resultStatus);
            
            if (success) {
                log.info("Image deleted successfully from Cloudinary: {}", publicId);
            } else {
                log.warn("Failed to delete image from Cloudinary: {}. Result: {}", publicId, result);
            }
            
            return success;
        } catch (IOException e) {
            log.error("Error deleting image from Cloudinary: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Extract Cloudinary public ID from URL
     * Example: https://res.cloudinary.com/your-cloud/image/upload/v1234567890/folder/image.jpg
     * Returns: folder/image
     */
    public String extractPublicIdFromUrl(String cloudinaryUrl) {
        if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
            return null;
        }

        try {
            // Split URL and find the part after "/upload/"
            String[] parts = cloudinaryUrl.split("/upload/");
            if (parts.length < 2) {
                return null;
            }

            // Get everything after version (v1234567890/)
            String afterUpload = parts[1];
            String[] versionParts = afterUpload.split("/", 2);
            if (versionParts.length < 2) {
                return afterUpload; // No version in URL
            }

            // Remove file extension
            String publicIdWithExtension = versionParts[1];
            return publicIdWithExtension.replaceAll("\\.[^.]+$", "");
            
        } catch (Exception e) {
            log.error("Error extracting public ID from URL: {}", cloudinaryUrl, e);
            return null;
        }
    }

    /**
     * Check if Cloudinary is configured and enabled
     */
    public boolean isCloudinaryAvailable() {
        return cloudinaryEnabled && cloudinary != null;
    }

    /**
     * Generate transformation URL for different image sizes
     * @param originalUrl Original Cloudinary URL
     * @param width Target width
     * @param height Target height
     * @return Transformed URL
     */
    public String getResizedImageUrl(String originalUrl, int width, int height) {
        if (!isCloudinaryAvailable() || originalUrl == null || originalUrl.isEmpty()) {
            return originalUrl;
        }

        try {
            // Insert transformation parameters into Cloudinary URL
            String transformation = String.format("c_fill,w_%d,h_%d,q_auto,f_auto", width, height);
            return originalUrl.replace("/upload/", "/upload/" + transformation + "/");
        } catch (Exception e) {
            log.error("Error generating resized image URL: {}", e.getMessage(), e);
            return originalUrl;
        }
    }
}
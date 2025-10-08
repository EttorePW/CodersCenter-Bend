package com.coderscenter.backend.controller;

import com.coderscenter.backend.services.ResendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

/**
 * Email Test Controller - for testing Resend API integration
 * Remove this controller in production!
 */
@RestController
@RequestMapping("/api/email/test")
@Slf4j
public class EmailTestController {

    @Autowired
    private ResendEmailService resendEmailService;

    /**
     * Test simple email sending
     * GET /api/email/test/simple?to=test@example.com&subject=Test&content=Hello
     */
    @GetMapping("/simple")
    public ResponseEntity<Map<String, Object>> testSimpleEmail(
            @RequestParam String to,
            @RequestParam(defaultValue = "Test Email") String subject,
            @RequestParam(defaultValue = "<h1>Test Email</h1><p>This is a test email from CodersCenter Backend!</p>") String content) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = resendEmailService.sendEmail(to, subject, content);
            
            if (success) {
                response.put("status", "success");
                response.put("message", "Email sent successfully");
                response.put("to", to);
                response.put("subject", subject);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Failed to send email");
                response.put("to", to);
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("Error testing email: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Exception occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Test welcome email
     * GET /api/email/test/welcome?to=test@example.com&userName=John&password=temp123
     */
    @GetMapping("/welcome")
    public ResponseEntity<Map<String, Object>> testWelcomeEmail(
            @RequestParam String to,
            @RequestParam String userName,
            @RequestParam String password) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = resendEmailService.sendWelcomeEmail(to, userName, password);
            
            if (success) {
                response.put("status", "success");
                response.put("message", "Welcome email sent successfully");
                response.put("to", to);
                response.put("userName", userName);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Failed to send welcome email");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("Error testing welcome email: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Exception occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Test password reset email
     * GET /api/email/test/password-reset?to=test@example.com&userName=John&newPassword=new123
     */
    @GetMapping("/password-reset")
    public ResponseEntity<Map<String, Object>> testPasswordResetEmail(
            @RequestParam String to,
            @RequestParam String userName,
            @RequestParam String newPassword) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = resendEmailService.sendPasswordResetEmail(to, userName, newPassword);
            
            if (success) {
                response.put("status", "success");
                response.put("message", "Password reset email sent successfully");
                response.put("to", to);
                response.put("userName", userName);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Failed to send password reset email");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("Error testing password reset email: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Exception occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Check email service configuration
     * GET /api/email/test/config
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> checkEmailConfig() {
        Map<String, Object> response = new HashMap<>();
        
        // Note: We don't expose the actual API key for security
        response.put("resendConfigured", resendEmailService != null);
        response.put("message", "Email service configuration check");
        response.put("note", "Use /simple endpoint to test actual email sending");
        
        return ResponseEntity.ok(response);
    }
}
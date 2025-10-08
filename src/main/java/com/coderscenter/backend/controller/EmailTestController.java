package com.coderscenter.backend.controller;

import com.coderscenter.backend.services.helperService.EmailSendingService;
import com.coderscenter.backend.components.EmailRespondGenerator;
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
    private EmailSendingService emailSendingService;
    
    @Autowired
    private EmailRespondGenerator emailRespondGenerator;

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
            boolean success = emailSendingService.sendEmailSync(to, subject, content);
            
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
            // Create a dummy user for email template
            com.coderscenter.backend.entities.profile.User dummyUser = new com.coderscenter.backend.entities.profile.User();
            dummyUser.setUsername(userName);
            dummyUser.setEmail(to);
            
            String emailBody = emailRespondGenerator.respondTextGenerator(dummyUser, password);
            boolean success = emailSendingService.sendEmailSync(to, "Willkommen bei CodersCenter!", emailBody);
            
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
     * Check email service configuration
     * GET /api/email/test/config
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> checkEmailConfig() {
        Map<String, Object> response = new HashMap<>();
        
        // Note: We don't expose the actual API key for security
        response.put("emailServiceConfigured", emailSendingService != null);
        response.put("message", "Email service configuration check - Using Resend API");
        response.put("note", "Use /simple or /welcome endpoint to test actual email sending");
        response.put("availableEndpoints", new String[]{
            "/api/email/test/simple",
            "/api/email/test/welcome",
            "/api/email/test/config"
        });
        
        return ResponseEntity.ok(response);
    }
}
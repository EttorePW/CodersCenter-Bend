package com.coderscenter.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Email Service using Resend.com API
 * Alternative to SMTP which is blocked by Render
 */
@Service
@Slf4j
public class ResendEmailService {

    @Value("${resend.api.key:}")
    private String resendApiKey;

    @Value("${resend.from.email:noreply@yourdomain.com}")
    private String fromEmail;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    public ResendEmailService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Send simple email via Resend API
     */
    public boolean sendEmail(String to, String subject, String htmlContent) {
        return sendEmail(to, subject, htmlContent, null);
    }

    /**
     * Send email with both HTML and text content
     */
    public boolean sendEmail(String to, String subject, String htmlContent, String textContent) {
        if (resendApiKey == null || resendApiKey.trim().isEmpty()) {
            log.warn("Resend API key not configured. Email not sent to: {}", to);
            return false;
        }

        try {
            // Prepare request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("from", fromEmail);
            requestBody.put("to", List.of(to));
            requestBody.put("subject", subject);
            requestBody.put("html", htmlContent);
            
            if (textContent != null && !textContent.trim().isEmpty()) {
                requestBody.put("text", textContent);
            }

            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);

            // Create request entity
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Send email
            ResponseEntity<String> response = restTemplate.postForEntity(
                RESEND_API_URL, 
                requestEntity, 
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Email sent successfully to: {}", to);
                return true;
            } else {
                log.error("Failed to send email to: {}. Status: {}, Response: {}", 
                    to, response.getStatusCode(), response.getBody());
                return false;
            }

        } catch (Exception e) {
            log.error("Error sending email to: {}. Error: {}", to, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Send welcome email for new users
     */
    public boolean sendWelcomeEmail(String to, String userName, String temporaryPassword) {
        String subject = "Willkommen bei CodersCenter!";
        String htmlContent = buildWelcomeEmailHtml(userName, temporaryPassword);
        String textContent = buildWelcomeEmailText(userName, temporaryPassword);
        
        return sendEmail(to, subject, htmlContent, textContent);
    }

    /**
     * Send password reset email
     */
    public boolean sendPasswordResetEmail(String to, String userName, String newPassword) {
        String subject = "CodersCenter - Neues Passwort";
        String htmlContent = buildPasswordResetEmailHtml(userName, newPassword);
        String textContent = buildPasswordResetEmailText(userName, newPassword);
        
        return sendEmail(to, subject, htmlContent, textContent);
    }

    // Private helper methods for email templates
    private String buildWelcomeEmailHtml(String userName, String temporaryPassword) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #4CAF50;">Willkommen bei CodersCenter!</h2>
                    <p>Hallo %s,</p>
                    <p>Dein Account wurde erfolgreich erstellt. Hier sind deine Login-Daten:</p>
                    <div style="background-color: #f4f4f4; padding: 15px; border-radius: 5px; margin: 20px 0;">
                        <p><strong>Temporäres Passwort:</strong> <code>%s</code></p>
                    </div>
                    <p><strong>Wichtig:</strong> Bitte ändere dein Passwort nach dem ersten Login.</p>
                    <p>Login-URL: <a href="https://dein-frontend.com/login">https://dein-frontend.com/login</a></p>
                    <br>
                    <p>Viel Erfolg bei CodersCenter!</p>
                    <p>Dein CodersCenter Team</p>
                </div>
            </body>
            </html>
            """.formatted(userName, temporaryPassword);
    }

    private String buildWelcomeEmailText(String userName, String temporaryPassword) {
        return """
            Willkommen bei CodersCenter!
            
            Hallo %s,
            
            Dein Account wurde erfolgreich erstellt. 
            
            Temporäres Passwort: %s
            
            Wichtig: Bitte ändere dein Passwort nach dem ersten Login.
            
            Login-URL: https://dein-frontend.com/login
            
            Viel Erfolg bei CodersCenter!
            Dein CodersCenter Team
            """.formatted(userName, temporaryPassword);
    }

    private String buildPasswordResetEmailHtml(String userName, String newPassword) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #2196F3;">Passwort zurückgesetzt</h2>
                    <p>Hallo %s,</p>
                    <p>Dein Passwort wurde erfolgreich zurückgesetzt:</p>
                    <div style="background-color: #f4f4f4; padding: 15px; border-radius: 5px; margin: 20px 0;">
                        <p><strong>Neues Passwort:</strong> <code>%s</code></p>
                    </div>
                    <p><strong>Wichtig:</strong> Bitte ändere dieses Passwort nach dem Login.</p>
                    <br>
                    <p>Dein CodersCenter Team</p>
                </div>
            </body>
            </html>
            """.formatted(userName, newPassword);
    }

    private String buildPasswordResetEmailText(String userName, String newPassword) {
        return """
            Passwort zurückgesetzt
            
            Hallo %s,
            
            Dein Passwort wurde erfolgreich zurückgesetzt.
            
            Neues Passwort: %s
            
            Wichtig: Bitte ändere dieses Passwort nach dem Login.
            
            Dein CodersCenter Team
            """.formatted(userName, newPassword);
    }
}
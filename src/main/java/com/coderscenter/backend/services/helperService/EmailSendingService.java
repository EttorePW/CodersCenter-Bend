package com.coderscenter.backend.services.helperService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendingService {

    @Value("${resend.api.key:}")
    private String resendApiKey;

    @Value("${resend.from.email:noreply@coderscenter.com}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            sendEmailSync(to, subject, body);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage(), e);
        }
    }

    /**
     * Synchronous email sending for immediate response
     */
    public boolean sendEmailSync(String to, String subject, String body) {
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
            requestBody.put("html", body);

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

            return response.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {
            log.error("Error sending email to: {}. Error: {}", to, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Legacy method for backwards compatibility
     * @deprecated Use sendEmailSync for immediate response checking
     */
    @Deprecated
    public void sendEmail(String to, String subject, String body, boolean isAsync) {
        if (isAsync) {
            sendEmail(to, subject, body);
        } else {
            sendEmailSync(to, subject, body);
        }
    }
}

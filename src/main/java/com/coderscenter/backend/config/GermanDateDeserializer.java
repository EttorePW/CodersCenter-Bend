package com.coderscenter.backend.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GermanDateDeserializer extends JsonDeserializer<LocalDate> {
    
    private static final DateTimeFormatter GERMAN_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {
        String dateString = p.getValueAsString();
        
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        dateString = dateString.trim();
        
        // Try to parse as Excel serial number first (if it's all digits)
        if (dateString.matches("\\d+")) {
            try {
                int excelSerial = Integer.parseInt(dateString);
                // Excel date serial starts from 1900-01-01 (but Excel considers 1900 a leap year, so we adjust)
                // Excel serial 1 = 1900-01-01, but LocalDate epoch is different
                LocalDate excelEpoch = LocalDate.of(1899, 12, 30); // Excel's epoch adjusted for the leap year bug
                return excelEpoch.plusDays(excelSerial);
            } catch (NumberFormatException | java.time.DateTimeException e) {
                // If Excel serial parsing fails, continue with other formats
            }
        }
        
        // Try German format (dd.MM.yyyy)
        try {
            return LocalDate.parse(dateString, GERMAN_FORMATTER);
        } catch (DateTimeParseException e1) {
            // If German format fails, try ISO format (yyyy-MM-dd)
            try {
                return LocalDate.parse(dateString, ISO_FORMATTER);
            } catch (DateTimeParseException e2) {
                throw new IOException("Unable to parse date: " + dateString + 
                    ". Expected format: dd.MM.yyyy, yyyy-MM-dd, or Excel serial number", e2);
            }
        }
    }
}
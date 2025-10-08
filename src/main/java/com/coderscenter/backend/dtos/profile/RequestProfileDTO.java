package com.coderscenter.backend.dtos.profile;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class RequestProfileDTO {

    private String firstName;
    private String lastName;

    @Pattern(
            regexp = "^\\+?[0-9]+$",
            message = "Telefonnummer darf nur Zahlen und '+' enthalten"
    )
    private String phone;

    @Past(message = "Geburtsdatum muss in der Vergangenheit liegen")
    private LocalDate birthDate;
}

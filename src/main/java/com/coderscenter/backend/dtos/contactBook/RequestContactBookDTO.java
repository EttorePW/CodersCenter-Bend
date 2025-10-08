package com.coderscenter.backend.dtos.contactBook;

import com.coderscenter.backend.enums.ApplicationChannel;
import com.coderscenter.backend.enums.ApplicationStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestContactBookDTO {

    @NotBlank(message = "Unternehmen darf nicht leer sein")
    private String companyName;

    private String contactPerson;

    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben")
    private String contactEmail;

    @Pattern(
            regexp = "^\\+?[0-9 ]*$",
            message = "Telefonnummer darf nur Zahlen, Leerzeichen und '+' enthalten"
    )
    private String contactPhone;

    @NotNull(message = "Bewerbungsquelle darf nicht leer sein")
    private ApplicationChannel applicationChannel;

    @NotNull(message = "Bewerbungsstatus darf nicht leer sein")
    private ApplicationStatus applicationStatus;

    @NotBlank(message = "Pfad zur Stellenanzeige darf nicht leer sein")
    private String adPath;
}

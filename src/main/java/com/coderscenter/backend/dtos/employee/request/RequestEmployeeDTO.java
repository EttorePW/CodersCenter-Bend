package com.coderscenter.backend.dtos.employee.request;

import com.coderscenter.backend.dtos.address.request.RequestAddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import com.coderscenter.backend.config.GermanDateDeserializer;
import com.coderscenter.backend.config.GermanDateSetDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.coderscenter.backend.enums.DayLabel;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestEmployeeDTO {

    @NotBlank(message = "Vorname darf nicht leer sein")
    private String firstName;

    @NotBlank(message = "Nachname darf nicht leer sein")
    private String lastName;

    @Email(message = "Bitte eine gültige E-Mail Adresse eingeben")
    @NotBlank(message = "E-Mail darf nicht leer sein")
    private String email;

    @Pattern(
            regexp = "^\\+?[0-9]+$",
            message = "Telefonnummer darf nur Zahlen und '+' enthalten"
    )
    @NotBlank(message = "Telefonnummer darf nicht leer sein")
    private String phone;

    @NotNull(message = "Geburtsdatum darf nicht leer sein")
    @Past(message = "Geburtsdatum muss in der Vergangenheit liegen")
    @JsonDeserialize(using = GermanDateDeserializer.class)
    private LocalDate birthDate;

    @NotBlank(message = "SVN darf nicht leer sein")
    @Size(min = 10, max = 10, message = "SVN muss genau 10 Ziffern enthalten")
    @Pattern(regexp = "^[0-9]+$", message = "SVN darf nur Ziffern enthalten")
    private String svn;

    @Positive(message = "Gehalt muss positiv sein")
    private Double salary;

    @NotNull(message = "Adresse darf nicht leer sein")
    @Valid
    private RequestAddressDTO address;

    private List<Long> subjects;

    // OptaPlanner Felder
    private Set<DayLabel> workDays;
    @JsonDeserialize(using = GermanDateSetDeserializer.class)
    private Set<LocalDate> holidays;
    @JsonDeserialize(using = GermanDateSetDeserializer.class)
    private Set<LocalDate> unavailableDates;
}

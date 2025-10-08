package com.coderscenter.backend.dtos.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class RequestPermissionDTO {

    @NotBlank(message = "Der Name der Permission darf nicht leer sein")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Startdatum darf nicht in der Vergangenheit liegen")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String description;

}

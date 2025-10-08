package com.coderscenter.backend.dtos.employee.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssignUserToEmployeeDTO {

    @NotNull(message = "Bitte einen Mitarbeiter auswählen")
    private Long employeeId;

    @NotNull(message = "Bitte einen User auswählen")
    private Long userId;
}

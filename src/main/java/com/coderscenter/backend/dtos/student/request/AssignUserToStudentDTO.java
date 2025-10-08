package com.coderscenter.backend.dtos.student.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssignUserToStudentDTO {

    @NotNull(message = "Bitte einen Studenten auswählen")
    private Long studentId;

    @NotNull(message = "Bitte einen User auswählen")
    private Long userId;
}

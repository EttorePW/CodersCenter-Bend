package com.coderscenter.backend.dtos.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FirstPasswordSetDTO {

    @NotBlank(message = "Neues Passwort darf nicht leer sein")
    @Size(min = 8, message = "Das Passwort muss mindesten 8 Zeichen lang sein")
    private String newPassword;

    @NotBlank(message = "Bestätigungspasswort darf nicht leer sein")
    private String confirmPassword;
}

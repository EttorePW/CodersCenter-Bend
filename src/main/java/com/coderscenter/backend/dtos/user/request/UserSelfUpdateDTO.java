package com.coderscenter.backend.dtos.user.request;

import jakarta.validation.constraints.Email;
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
public class UserSelfUpdateDTO {

    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben")
    @NotBlank(message = "E-Mail darf nicht leer sein")
    private String email;

    @Size(min = 8, message = "Das Passwort muss mindestens 8 Zeichen lang sein")
    private String password;

    private String confirmPassword;
}

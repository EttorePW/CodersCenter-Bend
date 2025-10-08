package com.coderscenter.backend.dtos.user.request;

import com.coderscenter.backend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAdminUpdateDTO {

    @Email (message = "Bitte eine gültige E-Mail-Adresse eingeben")
    @NotBlank(message = "E-Mail darf nicht leer sein")
    private String email;

    @Size(min = 8, message = "Das Passwort muss mindestens 8 Zeichen lang sein")
    private String password;

    private String confirmPassword;

    @NotNull(message = "Bitte eine Rolle auswählen")
    private Role role;

    private List<Long> permissions;
}

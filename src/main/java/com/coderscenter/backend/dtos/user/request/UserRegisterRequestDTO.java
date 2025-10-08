package com.coderscenter.backend.dtos.user.request;

import com.coderscenter.backend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegisterRequestDTO {

    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben")
    @NotBlank(message = "E-Mail darf nicht leer sein")
    private String email;

    @NotNull(message = "Bitte eine Rolle auswählen")
    private Role role;
}

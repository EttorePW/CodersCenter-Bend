package com.coderscenter.backend.dtos.address.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestAddressDTO {

    @NotBlank(message = "Straße darf nicht leer sein")
    private String street;

    @NotBlank(message = "Stadt darf nicht leer sein")
    private String city;

    @NotBlank(message = "PLZ darf nicht leer sein")
    @Pattern(
            regexp = "^[0-9]{4}$",
            message = "PLZ muss aus genau 4 Ziffern bestehen"
    )
    private String zip;
}

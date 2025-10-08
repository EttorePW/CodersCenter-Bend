package com.coderscenter.backend.dtos.profile;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ResponseProfileDTO {

    private Long profileId;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate birthDate;
    private String profileImageUrl;

}

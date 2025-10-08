package com.coderscenter.backend.dtos.student.response;


import com.coderscenter.backend.dtos.address.response.ResponseAddressDTO;
import com.coderscenter.backend.dtos.user.response.ResponseUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseStudentDTO {

    private Long studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String svn;
    private String amsOffice;

    private ResponseAddressDTO address;
    private ResponseUserDTO user;
}

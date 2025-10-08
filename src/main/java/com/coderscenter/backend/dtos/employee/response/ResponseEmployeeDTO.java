package com.coderscenter.backend.dtos.employee.response;

import com.coderscenter.backend.dtos.address.response.ResponseAddressDTO;
import com.coderscenter.backend.dtos.subject.ResponseSubjectDTO;
import com.coderscenter.backend.dtos.user.response.ResponseUserDTO;
import com.coderscenter.backend.enums.DayLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseEmployeeDTO {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String svn;
    private Double salary;

    private ResponseAddressDTO address;
    private ResponseUserDTO user;
    private List<Long> subjects;
    
    // Schedule-related fields
    private Set<DayLabel> workDays;
    private Set<LocalDate> holidays;
    private Set<LocalDate> unavailableDates;
}

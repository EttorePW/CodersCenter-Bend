package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.employee.request.RequestEmployeeDTO;
import com.coderscenter.backend.dtos.employee.response.ResponseEmployeeDTO;
import com.coderscenter.backend.entities.group_management.Subject;
import com.coderscenter.backend.entities.profile.Address;
import com.coderscenter.backend.entities.profile.Employee;
import com.coderscenter.backend.entities.profile.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final AddressMapper addressMapper;
    private final UserMapper userMapper;

    // === Employee to DTO ===
    public ResponseEmployeeDTO toResponseDTO(Employee employee) {
        if (employee == null) return null;
        // Von SubjectResponseDTO List to Long List
        List<Long> subjects = new ArrayList<>();
        employee.getSubjects().forEach(subject -> {
            subjects.add(subject.getSubjectId());
        });

        return ResponseEmployeeDTO.builder()
                .employeeId(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .birthDate(employee.getBirthDate())
                .salary(employee.getSalary())
                .svn(employee.getSvn())
                .address(addressMapper.toResponseDTO(employee.getAddress()))
                .user(employee.getUser() != null ? userMapper.toResponseDTO(employee.getUser()) : null)
                .subjects(subjects)
                .workDays(employee.getWorkDays())
                .holidays(employee.getHolidays())
                .unavailableDates(employee.getUnavailableDates())
                .build();
    }

    // === DTO to Employee ===
    public Employee toEmployee(RequestEmployeeDTO dto, Address address, User user, List<Subject> subjects) {
        if (dto == null) return null;

        return Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .svn(dto.getSvn())
                .salary(dto.getSalary())
                .address(address)
                .user(user)
                .subjects(subjects != null ? subjects : List.of())
                .workDays(dto.getWorkDays() != null ? dto.getWorkDays() : new HashSet<>())
                .holidays(dto.getHolidays() != null ? dto.getHolidays() : new HashSet<>())
                .unavailableDates(dto.getUnavailableDates() != null ? dto.getUnavailableDates() : new HashSet<>())
                .build();
    }
}

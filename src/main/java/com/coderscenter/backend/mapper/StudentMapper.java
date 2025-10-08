package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.student.request.RequestStudentDTO;
import com.coderscenter.backend.dtos.student.response.ResponseStudentDTO;
import com.coderscenter.backend.entities.profile.Address;
import com.coderscenter.backend.entities.profile.Student;
import com.coderscenter.backend.entities.profile.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final AddressMapper addressMapper;
    private final UserMapper userMapper;

    // === Entity to DTO ===
    public ResponseStudentDTO toResponseDTO(Student student) {
        if (student == null) return null;

        return ResponseStudentDTO.builder()
                .studentId(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .birthDate(student.getBirthDate())
                .svn(student.getSvn())
                .amsOffice(student.getAmsOffice())
                .address(addressMapper.toResponseDTO(student.getAddress()))
                .user(student.getUser() != null ? userMapper.toResponseDTO(student.getUser()) : null)
                .build();
    }

    // === DTO to Entity ===
    public Student toStudent(RequestStudentDTO dto, Address address, User user) {
        if (dto == null) return null;

        return Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .svn(dto.getSvn())
                .amsOffice(dto.getAmsOffice())
                .address(address)
                .user(user)
                .build();
    }
}

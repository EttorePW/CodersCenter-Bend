package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.contactBook.RequestContactBookDTO;
import com.coderscenter.backend.dtos.contactBook.ResponseContactBookDTO;
import com.coderscenter.backend.dtos.student.response.ResponseStudentDTO;
import com.coderscenter.backend.entities.management.ContactBook;
import com.coderscenter.backend.entities.profile.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactBookMapper {

    // === RequestDTO -> Entity ===
    public ContactBook toEntity(RequestContactBookDTO dto, Student student) {
        if (dto == null || student == null) {
            return null;
        }

        return ContactBook.builder()
                .companyName(dto.getCompanyName())
                .contactPerson(dto.getContactPerson())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .applicationChannel(dto.getApplicationChannel())
                .applicationStatus(dto.getApplicationStatus())
                .adPath(dto.getAdPath())
                .student(student) // Zuordnung Student
                .build();
    }

    // === Entity -> ResponseDTO ===
    public ResponseContactBookDTO toResponseDTO(ContactBook entity, ResponseStudentDTO studentDTO) {
        if (entity == null) {
            return null;
        }

        return ResponseContactBookDTO.builder()
                .contactBookId(entity.getId())
                .companyName(entity.getCompanyName())
                .contactPerson(entity.getContactPerson())
                .contactEmail(entity.getContactEmail())
                .contactPhone(entity.getContactPhone())
                .applicationChannel(entity.getApplicationChannel())
                .applicationStatus(entity.getApplicationStatus())
                .adPath(entity.getAdPath())
                .createdAt(entity.getCreatedAt())
                .student(studentDTO) // Student wird mit ResponseStudentDTO befüllt
                .build();
    }
}

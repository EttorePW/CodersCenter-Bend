package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.subject.ResponseSubjectDTO;
import com.coderscenter.backend.entities.group_management.Subject;
import org.springframework.stereotype.Service;

@Service
public class SubjectMapper {

    public ResponseSubjectDTO toResponseDTO(Subject subject) {

        return ResponseSubjectDTO.builder()
                .subjectId(subject.getSubjectId())
                .name(subject.getName())
                .build();

    }
}

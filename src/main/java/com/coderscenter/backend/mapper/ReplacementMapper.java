package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.replacements.ResponseReplacementDTO;
import com.coderscenter.backend.entities.management.Replacements;
import org.springframework.stereotype.Service;

@Service
public class ReplacementMapper {

    public ResponseReplacementDTO toResponseDTO(Replacements replacements) {
        return ResponseReplacementDTO.builder()
                .replacementId(replacements.getReplacementId())
                .employeeId(replacements.getEmployeeId())
                .employeeName(replacements.getEmployeeName())
                .slotId(replacements.getSlotId())
                .subjectName(replacements.getSubjectName())
                .startDate(replacements.getStartDate())
                .endDate(replacements.getEndDate())
                .reason(replacements.getReason())
                .build();
    }
}

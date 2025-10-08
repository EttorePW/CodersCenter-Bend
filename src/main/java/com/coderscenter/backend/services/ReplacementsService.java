package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.attendance.RequestAttendanceDTO;
import com.coderscenter.backend.dtos.replacements.RequestReplacementDTO;
import com.coderscenter.backend.dtos.replacements.ResponseReplacementDTO;
import com.coderscenter.backend.entities.management.Replacements;
import com.coderscenter.backend.mapper.ReplacementMapper;
import com.coderscenter.backend.repositories.ReplacementsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplacementsService {

    private final ReplacementsRepository replacementsRepository;
    private final ReplacementMapper replacementMapper;


    public List<ResponseReplacementDTO> getAllReplacements() {
        List<Replacements> replacements = replacementsRepository.findAll();
        List <ResponseReplacementDTO> responseReplacementDTOS = new ArrayList<>();
        replacements.forEach(replacement -> {
            responseReplacementDTOS.add(replacementMapper.toResponseDTO(replacement));
        });

        return responseReplacementDTOS;
    }

    public ResponseReplacementDTO getReplacementById(Long id) {
        Replacements replacement = replacementsRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechende Vertretungsangfrage in der Datenbank gefunden!"));
        return replacementMapper.toResponseDTO(replacement);
    }


    public ResponseReplacementDTO createNew(RequestReplacementDTO requestReplacementDTO) {
        Replacements replacements = Replacements.builder()
                .employeeId(requestReplacementDTO.getEmployeeId())
                .employeeName(requestReplacementDTO.getEmployeeName())
                .slotId(requestReplacementDTO.getSlotId())
                .subjectName(requestReplacementDTO.getSubjectName())
                .startDate(requestReplacementDTO.getStartDate())
                .endDate(requestReplacementDTO.getEndDate())
                .reason(requestReplacementDTO.getReason())
                .build();

        replacementsRepository.save(replacements);
        return replacementMapper.toResponseDTO(replacements);
    }
    public ResponseReplacementDTO updateReplacement(Long id, RequestReplacementDTO requestReplacementDTO) {
        Replacements replacements = replacementsRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechende Vertretungsangfrage in der Datenbank gefunden!"));

        replacements.setEmployeeId(requestReplacementDTO.getEmployeeId());
        replacements.setEmployeeName(requestReplacementDTO.getEmployeeName());
        replacements.setSlotId(requestReplacementDTO.getSlotId());
        replacements.setSubjectName(requestReplacementDTO.getSubjectName());
        replacements.setStartDate(requestReplacementDTO.getStartDate());
        replacements.setEndDate(requestReplacementDTO.getEndDate());
        replacements.setReason(requestReplacementDTO.getReason());

        replacementsRepository.save(replacements);
        return replacementMapper.toResponseDTO(replacements);
    }
    public Boolean deleteReplacement(Long id) {
        Replacements replacements = replacementsRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechende Vertretungsangfrage in der Datenbank gefunden!"));
        replacementsRepository.delete(replacements);
        return true;
    }
}

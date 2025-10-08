package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.slot.ResponseSlotDTO;
import com.coderscenter.backend.entities.schedule_management.Slot;
import com.coderscenter.backend.services.helperService.DateParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlotMapper {

    private final SubjectMapper subjectMapper;
    private final EmployeeMapper employeeMapper;

    public ResponseSlotDTO toResponseDTO(Slot slot) {
        return ResponseSlotDTO.builder()
                .slotId(slot.getSlotId())
                .slotTopic(slot.getSlotTopic())
                .startDate(slot.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .endDate(slot.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .dayId(slot.getDay().getDayId())
                .subject(subjectMapper.toResponseDTO(slot.getSubject()))
                .employee(employeeMapper.toResponseDTO(slot.getEmployee()))
                .build();
    }

    public List<ResponseSlotDTO> toResponseDTOList(List<Slot> slots) {
        return slots.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }


}

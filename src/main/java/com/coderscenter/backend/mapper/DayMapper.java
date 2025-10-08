package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.day.ResponseDayDTO;
import com.coderscenter.backend.entities.schedule_management.Day;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DayMapper {

    private final SlotMapper slotMapper;

    public ResponseDayDTO toResponseDTO(Day day) {
        return ResponseDayDTO.builder()
                .dayId(day.getDayId())
                .label(day.getLabel())
                .dayDate(day.getDayDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .weekId(day.getWeek().getWeekId())
                .slots(slotMapper.toResponseDTOList(day.getSlots()))
                .build();
    }

    public List<ResponseDayDTO> toResponseDTOList(List<Day> days) {
        return days.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}

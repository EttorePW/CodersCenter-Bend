package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.week.ResponseWeekDTO;
import com.coderscenter.backend.entities.schedule_management.Week;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeekMapper {

    private final DayMapper dayMapper;


    public ResponseWeekDTO toResponseDTO(Week week) {
        return ResponseWeekDTO.builder()
                .weekId(week.getWeekId())
                .label(week.getLabel())
                .weekStartDate(week.getWeekStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .schedule(week.getSchedule().getScheduleId())
                .days(dayMapper.toResponseDTOList(week.getDays())).build();
    }

    public List<ResponseWeekDTO> toResponseDTOList(List<Week> weeks) {
        return weeks.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}

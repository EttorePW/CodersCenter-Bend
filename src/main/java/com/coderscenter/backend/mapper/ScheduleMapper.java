package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.schedule.ResponseScheduleDTO;
import com.coderscenter.backend.entities.schedule_management.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleMapper {

    private final WeekMapper weekMapper;
    private final GroupMapper groupMapper;

    public ResponseScheduleDTO toResponseDTO(Schedule schedule) {
        return ResponseScheduleDTO.builder()
                .scheduleId(schedule.getScheduleId())
                .group(groupMapper.toResponseWithListDTO(schedule.getGroup()))
                .weeks(weekMapper.toResponseDTOList(schedule.getWeeks()))
                .build();
    }

}

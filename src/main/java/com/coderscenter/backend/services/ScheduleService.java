package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.schedule.RequestScheduleDTO;
import com.coderscenter.backend.dtos.schedule.ResponseScheduleDTO;
import com.coderscenter.backend.dtos.week.RequestWeekDTO;
import com.coderscenter.backend.entities.group_management.Group;
import com.coderscenter.backend.entities.schedule_management.Schedule;
import com.coderscenter.backend.entities.schedule_management.Week;
import com.coderscenter.backend.mapper.ScheduleMapper;
import com.coderscenter.backend.mapper.WeekMapper;
import com.coderscenter.backend.repositories.GroupRepository;
import com.coderscenter.backend.repositories.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final WeekService weekService;
    private final WeekMapper weekMapper;
    private final ScheduleMapper scheduleMapper;

    public List<ResponseScheduleDTO> getAllSchedules() {
        
        List<ResponseScheduleDTO> responseScheduleDTOList = new ArrayList<>();
        scheduleRepository.findAll().forEach(schedule -> {
            responseScheduleDTOList.add(scheduleMapper.toResponseDTO(schedule));
        });
        
        return responseScheduleDTOList;
    }

    public ResponseScheduleDTO getScheduleById(Long id) {
        
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Schedule in der Datenbank gefunden!"));
        
        return scheduleMapper.toResponseDTO(schedule);
        
    }

    public ResponseScheduleDTO createNew(RequestScheduleDTO requestScheduleDTO) {
        Group group = groupRepository.findById(requestScheduleDTO.getGroupId()).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Group in der Datenbank gefunden!"));
        List<Week> weeks = new ArrayList<>();

        Schedule schedule = Schedule.builder()
                .group(group)
                .build();

        requestScheduleDTO.getWeeks().forEach(week -> {
            weeks.add(weekService.createNew(week, schedule));
        });

        schedule.setWeeks(weeks);
        scheduleRepository.save(schedule);

        return scheduleMapper.toResponseDTO(schedule);
    }


    public ResponseScheduleDTO addWeek(Long id, RequestWeekDTO requestWeekDTO) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Schedule in der Datenbank gefunden!"));
        Week week = weekService.createNew(requestWeekDTO, schedule);

        schedule.getWeeks().add(week);
        scheduleRepository.save(schedule);

        return scheduleMapper.toResponseDTO(schedule);
    }

    public String deleteSchedule(Long id) {

        scheduleRepository.deleteById(id);
        return "Schedule " + id + " successfully deleted!";

    }
}

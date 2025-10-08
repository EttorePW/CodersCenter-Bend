package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.week.RequestWeekDTO;
import com.coderscenter.backend.dtos.week.ResponseWeekDTO;
import com.coderscenter.backend.entities.schedule_management.Day;
import com.coderscenter.backend.entities.schedule_management.Schedule;
import com.coderscenter.backend.entities.schedule_management.Week;
import com.coderscenter.backend.mapper.WeekMapper;
import com.coderscenter.backend.repositories.ScheduleRepository;
import com.coderscenter.backend.repositories.WeekRepository;
import com.coderscenter.backend.services.helperService.DateParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekService {

    private final WeekRepository weekRepository;
    private final ScheduleRepository scheduleRepository;
    private final DayService dayService;
    private final WeekMapper weekMapper;
    private final DateParseService dateParseService;

    public List<ResponseWeekDTO> getAllWeeks() {

        List<Week> weeks = weekRepository.findAll();
        List<ResponseWeekDTO> responseWeekDTOS = new ArrayList<>();

        weeks.forEach(week -> {
            responseWeekDTOS.add(weekMapper.toResponseDTO(week));
        });

        return responseWeekDTOS;
    }

    public ResponseWeekDTO getWeekById(Long id) {

        Week week = weekRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechende Week in der Datenbank gefunden!"));
        return weekMapper.toResponseDTO(week);

    }

    public Week createNew(RequestWeekDTO requestWeekDTO, Schedule schedule) {

        List<Day> days = new ArrayList<>();

        Week week = Week.builder()
                .label(requestWeekDTO.getLabel())
                .weekStartDate(dateParseService.stringToLocalDate(requestWeekDTO.getWeekStartDate()).atStartOfDay())
                .schedule(schedule)
                .build();

        requestWeekDTO.getDays().forEach(day -> {
            days.add(dayService.createNew(day, week));
        });

        week.setDays(days);

        return week;
    }

    public ResponseWeekDTO updateWeek(Long id, RequestWeekDTO requestWeekDTO) {
        Week week = weekRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechende Week in der Datenbank gefunden!"));

        week.setLabel(requestWeekDTO.getLabel());
        week.setWeekStartDate(dateParseService.stringToLocalDate(requestWeekDTO.getWeekStartDate()).atStartOfDay());

        // Hier noch ergänzen mit möglichen Veränderungen

        return weekMapper.toResponseDTO(week);

    }

    public String deleteWeek(Long id) {

        weekRepository.deleteById(id);
        return "Week " + id + " successfully deleted!";

    }
}

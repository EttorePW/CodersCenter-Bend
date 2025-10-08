package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.day.RequestDayDTO;
import com.coderscenter.backend.dtos.day.ResponseDayDTO;
import com.coderscenter.backend.entities.schedule_management.Day;
import com.coderscenter.backend.entities.schedule_management.Slot;
import com.coderscenter.backend.entities.schedule_management.Week;
import com.coderscenter.backend.mapper.DayMapper;
import com.coderscenter.backend.repositories.DayRepository;
import com.coderscenter.backend.repositories.WeekRepository;
import com.coderscenter.backend.services.helperService.DateParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DayService {

    private final DayRepository dayRepository;
    private final WeekRepository weekRepository;
    private final SlotService slotService;
    private final DayMapper dayMapper;
    private final DateParseService dateParseService;

    public List<ResponseDayDTO> getAllDays() {
        List<ResponseDayDTO> responseDayDTOList = new ArrayList<>();
        List<Day> days = dayRepository.findAll();

        days.forEach(day -> {
            responseDayDTOList.add(dayMapper.toResponseDTO(day));
        });

        return responseDayDTOList;
    }

    public ResponseDayDTO getDayById(Long id)  {

        Day day = dayRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Day in der Datenbank gefunden!"));

        return dayMapper.toResponseDTO(day);

    }


    public Day createNew(RequestDayDTO requestDayDTO, Week week) {
        List<Slot> slotList = new ArrayList<>();

        Day day = Day.builder()
                .label(requestDayDTO.getLabel())
                .dayDate(dateParseService.stringToLocalDate(requestDayDTO.getDayDate()).atStartOfDay())
                .week(week)
                .build();

        requestDayDTO.getSlots().forEach(slot -> {
            slotList.add(slotService.createNew(slot, day));
        });

        day.setSlots(slotList);

        return day;
    }


    public ResponseDayDTO updateDay(Long id, RequestDayDTO requestDayDTO) {
        Day day = dayRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Day in der Datenbank gefunden!"));

        day.setLabel(requestDayDTO.getLabel());
        day.setDayDate(dateParseService.stringToLocalDate(requestDayDTO.getDayDate()).atStartOfDay());

        // Hier noch ergänzen mit möglichen Veränderungen

        dayRepository.save(day);

        return dayMapper.toResponseDTO(day);
    }

    public String deleteDay(Long id) {

        dayRepository.deleteById(id);
        return "Day " + id + " successfully deleted!";

    }
}

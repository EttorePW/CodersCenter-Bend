package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.schedule.RequestScheduleDTO;
import com.coderscenter.backend.dtos.week.RequestWeekDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(scheduleService.getAllSchedules(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws UsernameNotFoundException {
        return new ResponseEntity<>(scheduleService.getScheduleById(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postNew(@RequestBody RequestScheduleDTO requestScheduleDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(scheduleService.createNew(requestScheduleDTO), HttpStatus.CREATED);
    }


    @PutMapping("/{id}/addweek")
    public ResponseEntity<?> addNewWeek(@PathVariable Long id, @RequestBody RequestWeekDTO requestWeekDTO) throws UsernameNotFoundException {
        return new ResponseEntity<>(scheduleService.addWeek(id, requestWeekDTO),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws EmptyOptionalException {
        return new ResponseEntity<>(scheduleService.deleteSchedule(id),HttpStatus.OK);
    }

}

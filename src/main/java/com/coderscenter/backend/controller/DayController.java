package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.day.RequestDayDTO;
import com.coderscenter.backend.dtos.day.ResponseDayDTO;
import com.coderscenter.backend.dtos.slot.RequestSlotDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.mapper.DayMapper;
import com.coderscenter.backend.services.DayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/schedule/day")
@RequiredArgsConstructor
public class DayController {

    private final DayService dayService;
    private final DayMapper dayMapper;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(dayService.getAllDays(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws UsernameNotFoundException {
        return new ResponseEntity<>(dayService.getDayById(id),HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<?> postNew(@RequestBody RequestDayDTO requestDayDTO) throws UsernameNotFoundException, EmptyOptionalException {
//        return new ResponseEntity<>(dayMapper.toResponseDTO(dayService.createNew(requestDayDTO)), HttpStatus.CREATED);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestDayDTO requestDayDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(dayService.updateDay(id, requestDayDTO),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws EmptyOptionalException {
        return new ResponseEntity<>(dayService.deleteDay(id),HttpStatus.OK);
    }
}

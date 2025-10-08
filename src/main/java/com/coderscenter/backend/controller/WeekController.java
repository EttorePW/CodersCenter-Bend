package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.week.RequestWeekDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.mapper.WeekMapper;
import com.coderscenter.backend.services.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/schedule/week")
@RequiredArgsConstructor
public class WeekController {

    private final WeekService weekService;
    private final WeekMapper weekMapper;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(weekService.getAllWeeks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws UsernameNotFoundException {
        return new ResponseEntity<>(weekService.getWeekById(id),HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<?> postNew(@RequestBody RequestWeekDTO requestWeekDTO) throws UsernameNotFoundException, EmptyOptionalException {
//        return new ResponseEntity<>(weekMapper.toResponseDTO(weekService.createNew(requestWeekDTO)), HttpStatus.CREATED);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestWeekDTO requestWeekDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(weekService.updateWeek(id, requestWeekDTO),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws EmptyOptionalException {
        return new ResponseEntity<>(weekService.deleteWeek(id),HttpStatus.OK);
    }
}

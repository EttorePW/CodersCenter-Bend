package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.attendance.RequestAttendanceDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService absenceService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(absenceService.getAllAttendances(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws UsernameNotFoundException {
        return new ResponseEntity<>(absenceService.getAttendanceById(id),HttpStatus.OK);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getByStudentId(@PathVariable Long id) throws UsernameNotFoundException {
        return new ResponseEntity<>(absenceService.getAttendanceByStudentId(id),HttpStatus.OK);
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<?> getByGroupId(@PathVariable Long id) throws UsernameNotFoundException {
        return new ResponseEntity<>(absenceService.getAttendanceByGroupId(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postNew(@RequestBody RequestAttendanceDTO requestAttendanceDTO) throws UsernameNotFoundException {
        return new ResponseEntity<>(absenceService.createNew(requestAttendanceDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestAttendanceDTO requestAttendanceDTO) throws UsernameNotFoundException {
        return new ResponseEntity<>(absenceService.updateAttendance(id, requestAttendanceDTO),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws EmptyOptionalException {
        return new ResponseEntity<>(absenceService.deleteAttendance(id),HttpStatus.OK);
    }
}

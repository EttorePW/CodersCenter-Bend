package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.attendance.RequestAttendanceDTO;
import com.coderscenter.backend.dtos.replacements.RequestReplacementDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.services.ReplacementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/replacements")
@RequiredArgsConstructor
public class ReplacementsController {

    private final ReplacementsService replacementsService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(replacementsService.getAllReplacements(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws UsernameNotFoundException {
        return new ResponseEntity<>(replacementsService.getReplacementById(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postNew(@RequestBody RequestReplacementDTO requestReplacementDTO) throws UsernameNotFoundException {
        return new ResponseEntity<>(replacementsService.createNew(requestReplacementDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestReplacementDTO requestReplacementDTO) throws UsernameNotFoundException {
        return new ResponseEntity<>(replacementsService.updateReplacement(id, requestReplacementDTO),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws EmptyOptionalException {
        return new ResponseEntity<>(replacementsService.deleteReplacement(id),HttpStatus.OK);
    }

}

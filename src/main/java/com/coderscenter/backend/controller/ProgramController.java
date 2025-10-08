package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.program.RequestProgramDTO;
import com.coderscenter.backend.dtos.subject.RequestSubjectDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.services.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/program")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;


    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(programService.getAllPrograms(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(programService.getProgramById(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postNew(@RequestBody RequestProgramDTO requestProgramDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(programService.createNew(requestProgramDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestProgramDTO requestProgramDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(programService.updateProgram(id, requestProgramDTO),HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws EmptyOptionalException {
        return new ResponseEntity<>(programService.deleteProgram(id),HttpStatus.OK);
    }
}

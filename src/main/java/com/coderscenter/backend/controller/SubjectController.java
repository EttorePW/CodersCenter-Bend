package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.subject.RequestSubjectDTO;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(subjectService.getAllPermission(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(subjectService.getPermissionById(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postNew(@RequestBody RequestSubjectDTO subjectDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(subjectService.createNew(subjectDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestSubjectDTO subjectDTO) throws UsernameNotFoundException, EmptyOptionalException {
        return new ResponseEntity<>(subjectService.updateSubject(id, subjectDTO),HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws EmptyOptionalException {
        return new ResponseEntity<>(subjectService.deleteSubject(id),HttpStatus.OK);
    }

}

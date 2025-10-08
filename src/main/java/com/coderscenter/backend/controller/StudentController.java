package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.student.request.RequestStudentDTO;
import com.coderscenter.backend.dtos.student.request.UpdateStudentDTO;
import com.coderscenter.backend.dtos.student.response.ResponseStudentDTO;
import com.coderscenter.backend.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<ResponseStudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ResponseStudentDTO> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseStudentDTO> getStudentByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(studentService.getStudentByUserId(userId));
    }

    @PostMapping("/administration")
    public ResponseEntity<ResponseStudentDTO> createStudent(@Valid @RequestBody RequestStudentDTO requestStudentDTO) {
        return new ResponseEntity<>(studentService.createStudent(requestStudentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{studentId}/user/{userId}")
    public ResponseEntity<ResponseStudentDTO> connectUserAndStudent(@PathVariable Long studentId,
                                                                    @PathVariable Long userId) {
        return ResponseEntity.ok(studentService.connectUserAndStudent(studentId, userId));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<ResponseStudentDTO> updateStudent(@PathVariable Long studentId,
                                                            @RequestBody UpdateStudentDTO updateStudentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(studentId, updateStudentDTO));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> delete(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}

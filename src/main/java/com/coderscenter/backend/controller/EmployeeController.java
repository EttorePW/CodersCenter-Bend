package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.employee.request.RequestEmployeeDTO;
import com.coderscenter.backend.dtos.employee.request.UpdateEmployeeDTO;
import com.coderscenter.backend.dtos.employee.response.ResponseEmployeeDTO;
import com.coderscenter.backend.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<ResponseEmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseEmployeeDTO> getEmployeeById(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseEmployeeDTO> getEmployeeByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(employeeService.getEmployeeByUserId(userId));
    }

    @PostMapping("/administration")
    public ResponseEntity<ResponseEmployeeDTO> createEmployee(@Valid @RequestBody RequestEmployeeDTO requestEmployeeDTO) {
        return new ResponseEntity<>(employeeService.createEmployee(requestEmployeeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}/user/{userId}")
    public ResponseEntity<ResponseEmployeeDTO> connectUserAndEmployee(@PathVariable Long employeeId,
                                                                      @PathVariable Long userId) {
        return ResponseEntity.ok(employeeService.connectUserAndEmployee(employeeId, userId));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<ResponseEmployeeDTO> updateEmployee(@PathVariable Long employeeId,
                                                              @RequestBody UpdateEmployeeDTO updateEmployeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, updateEmployeeDTO));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}

package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.address.response.ResponseAddressDTO;
import com.coderscenter.backend.dtos.employee.request.RequestEmployeeDTO;
import com.coderscenter.backend.dtos.employee.request.UpdateEmployeeDTO;
import com.coderscenter.backend.dtos.employee.response.ResponseEmployeeDTO;
import com.coderscenter.backend.entities.group_management.Subject;
import com.coderscenter.backend.entities.profile.Address;
import com.coderscenter.backend.entities.profile.Employee;
import com.coderscenter.backend.entities.profile.User;
import com.coderscenter.backend.exceptions.*;
import com.coderscenter.backend.mapper.EmployeeMapper;
import com.coderscenter.backend.repositories.AddressRepository;
import com.coderscenter.backend.repositories.EmployeeRepository;
import com.coderscenter.backend.repositories.SubjectRepository;
import com.coderscenter.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final SubjectRepository subjectRepository;
    private final AddressService addressService;
    private final EmployeeMapper employeeMapper;

    /**
     * Get all employees from the database and return them as DTOs.
     *
     * @return List of {@link ResponseEmployeeDTO}
     */
    public List<ResponseEmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<ResponseEmployeeDTO> responseEmployeeDTOs = new ArrayList<>();
        employees.forEach(e -> responseEmployeeDTOs.add(employeeMapper.toResponseDTO(e)));
        return responseEmployeeDTOs;
    }

    /**
     * Get one employee by its ID.
     *
     * @param employeeId ID of the employee
     * @return {@link ResponseEmployeeDTO}
     * @throws EmployeeNotFoundException if employee not found
     */
    public ResponseEmployeeDTO getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee mit ID " + employeeId + " nicht gefunden."));
        return employeeMapper.toResponseDTO(employee);
    }

    /**
     * Get an employee by the linked user ID.
     *
     * @param userId ID of the user
     * @return {@link ResponseEmployeeDTO}
     * @throws UserNotFoundException     if user not found
     * @throws EmployeeNotFoundException if no employee linked to the user
     */
    public ResponseEmployeeDTO getEmployeeByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Kein User mit ID " + userId + " gefunden");
        }
        Employee employee = employeeRepository.findEmployeeByUser_Id(userId);
        if (employee == null) {
            throw new EmployeeNotFoundException("Kein Employee für User-ID " + userId + " gefunden");
        }
        return employeeMapper.toResponseDTO(employee);
    }

    /**
     * Create a new employee (with a new address).
     *
     * @param requestEmployeeDTO DTO containing employee details
     * @return {@link ResponseEmployeeDTO} with created employee
     */
    public ResponseEmployeeDTO createEmployee(RequestEmployeeDTO requestEmployeeDTO) {
        ResponseAddressDTO addressDTO = addressService.createNew(requestEmployeeDTO.getAddress());
        Address address = addressRepository.findById(addressDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Adresse mit ID " + addressDTO.getAddressId() + " nicht gefunden"));

        List<Subject> subjects = subjectRepository.findAllById(requestEmployeeDTO.getSubjects());
        Employee employee = employeeMapper.toEmployee(requestEmployeeDTO, address, null, subjects);

        employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(employee);
    }

    /**
     * Connect an existing employee with an existing user.
     *
     * @param employeeId ID of the employee
     * @param userId     ID of the user
     * @return {@link ResponseEmployeeDTO} with updated employee
     * @throws UserNotFoundException     if user not found
     * @throws EmployeeNotFoundException if employee not found
     */
    public ResponseEmployeeDTO connectUserAndEmployee(Long employeeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Kein User mit ID " + userId + " gefunden"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee mit ID " + employeeId + " nicht gefunden"));

        employee.setUser(user);
        employeeRepository.save(employee);

        return employeeMapper.toResponseDTO(employee);
    }

    /**
     * Update the details of an existing employee.
     *
     * @param employeeId ID of the employee
     * @param dto        DTO with fields to update
     * @return {@link ResponseEmployeeDTO} with updated details
     * @throws EmployeeNotFoundException if employee not found
     */
    public ResponseEmployeeDTO updateEmployee(Long employeeId, UpdateEmployeeDTO dto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee mit ID " + employeeId + " nicht gefunden"));

        if (dto.getFirstName() != null) employee.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) employee.setLastName(dto.getLastName());
        if (dto.getEmail() != null) employee.setEmail(dto.getEmail());
        if (dto.getPhone() != null) employee.setPhone(dto.getPhone());
        if (dto.getBirthDate() != null) employee.setBirthDate(dto.getBirthDate());
        if (dto.getSvn() != null) employee.setSvn(dto.getSvn());
        if (dto.getSalary() != null) employee.setSalary(dto.getSalary());

        if (dto.getAddress() != null && employee.getAddress() != null) {
            if (dto.getAddress().getStreet() != null) employee.getAddress().setStreet(dto.getAddress().getStreet());
            if (dto.getAddress().getCity() != null) employee.getAddress().setCity(dto.getAddress().getCity());
            if (dto.getAddress().getZip() != null) employee.getAddress().setZip(dto.getAddress().getZip());
        }

        if (dto.getSubjects() != null) {
            List<Subject> subjects = subjectRepository.findAllById(dto.getSubjects());
            employee.setSubjects(subjects);
        }

        employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(employee);
    }

    /**
     * Delete an employee by its ID.
     *
     * @param id of the employee
     * @throws EmployeeNotFoundException if employee not found
     */
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee mit ID " + id + " nicht gefunden"));
        employeeRepository.delete(employee);
    }
}

package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.address.response.ResponseAddressDTO;
import com.coderscenter.backend.dtos.student.request.RequestStudentDTO;
import com.coderscenter.backend.dtos.student.request.UpdateStudentDTO;
import com.coderscenter.backend.dtos.student.response.ResponseStudentDTO;
import com.coderscenter.backend.entities.profile.Address;
import com.coderscenter.backend.entities.profile.Student;
import com.coderscenter.backend.entities.profile.User;
import com.coderscenter.backend.exceptions.*;
import com.coderscenter.backend.mapper.StudentMapper;
import com.coderscenter.backend.repositories.AddressRepository;
import com.coderscenter.backend.repositories.StudentRepository;
import com.coderscenter.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final StudentMapper studentMapper;

    /**
     * Get all students from the database and return them as DTOs.
     *
     * @return List of {@link ResponseStudentDTO}
     */
    public List<ResponseStudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<ResponseStudentDTO> responseStudentDTOS = new ArrayList<>();
        students.forEach(student -> responseStudentDTOS.add(studentMapper.toResponseDTO(student)));
        return responseStudentDTOS;
    }

    /**
     * Get one student by its ID.
     *
     * @param studentId ID of the student
     * @return {@link ResponseStudentDTO} with details
     * @throws ResourceNotFoundException if no student is found
     */
    public ResponseStudentDTO getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student mit ID " + studentId + " nicht gefunden."));
        return studentMapper.toResponseDTO(student);
    }

    /**
     * Get the student that is linked with a given user ID.
     *
     * @param userId ID of the user
     * @return {@link ResponseStudentDTO}
     * @throws UserNotFoundException     if no user with the given ID exists
     * @throws ResourceNotFoundException if no student is linked with that user
     */
    public ResponseStudentDTO getStudentByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Kein User mit ID " + userId + " gefunden");
        }
        Student student = studentRepository.findStudentByUser_Id(userId);
        if (student == null) {
            throw new StudentNotFoundException("Kein Student für User-ID " + userId + " gefunden");
        }
        return studentMapper.toResponseDTO(student);
    }

    /**
     * Create a new student (with a new address).
     *
     * @param requestStudentDTO DTO containing student details
     * @return {@link ResponseStudentDTO} with created student
     */
    public ResponseStudentDTO createStudent(RequestStudentDTO requestStudentDTO) {
        ResponseAddressDTO addressDTO = addressService.createNew(requestStudentDTO.getAddress());
        Address address = addressRepository.findById(addressDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Adresse mit ID " + addressDTO.getAddressId() + " nicht gefunden"));

        Student student = studentMapper.toStudent(requestStudentDTO, address, null);
        studentRepository.save(student);
        return studentMapper.toResponseDTO(student);
    }

    /**
     * Connect an existing student with an existing user.
     *
     * @param studentId ID of the student
     * @param userId    ID of the existing user to connect
     * @return {@link ResponseStudentDTO} with updated student
     * @throws UserNotFoundException     if user cannot be found
     * @throws ResourceNotFoundException if student cannot be found
     */
    public ResponseStudentDTO connectUserAndStudent(Long studentId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with id " + userId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("No student found with id " + studentId));

        student.setUser(user);
        studentRepository.save(student);

        return studentMapper.toResponseDTO(student);
    }

    /**
     * Update the details of an existing student.
     *
     * @param studentId ID of the student
     * @param dto       DTO with fields to update
     * @return {@link ResponseStudentDTO} with updated details
     * @throws ResourceNotFoundException if student cannot be found
     */
    public ResponseStudentDTO updateStudent(Long studentId, UpdateStudentDTO dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("No student found with id " + studentId));

        if (dto.getFirstName() != null) student.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) student.setLastName(dto.getLastName());
        if (dto.getEmail() != null) student.setEmail(dto.getEmail());
        if (dto.getPhone() != null) student.setPhone(dto.getPhone());
        if (dto.getBirthDate() != null) student.setBirthDate(dto.getBirthDate());
        if (dto.getSvn() != null) student.setSvn(dto.getSvn());
        if (dto.getAmsOffice() != null) student.setAmsOffice(dto.getAmsOffice());

        if (dto.getAddress() != null && student.getAddress() != null) {
            if (dto.getAddress().getStreet() != null) student.getAddress().setStreet(dto.getAddress().getStreet());
            if (dto.getAddress().getCity() != null) student.getAddress().setCity(dto.getAddress().getCity());
            if (dto.getAddress().getZip() != null) student.getAddress().setZip(dto.getAddress().getZip());
        }

        studentRepository.save(student);
        return studentMapper.toResponseDTO(student);
    }

    /**
     * Delete a student by its ID.
     *
     * @param id of the student
     * @throws StudentNotFoundException if student not found
     */
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student mit ID " + id + " nicht gefunden"));
        studentRepository.delete(student);
    }
}

package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.contactBook.RequestContactBookDTO;
import com.coderscenter.backend.dtos.contactBook.ResponseContactBookDTO;
import com.coderscenter.backend.dtos.student.response.ResponseStudentDTO;
import com.coderscenter.backend.entities.management.ContactBook;
import com.coderscenter.backend.entities.profile.Student;
import com.coderscenter.backend.exceptions.ResourceNotFoundException;
import com.coderscenter.backend.exceptions.StudentNotFoundException;
import com.coderscenter.backend.mapper.ContactBookMapper;
import com.coderscenter.backend.mapper.StudentMapper;
import com.coderscenter.backend.repositories.ContactBookRepository;
import com.coderscenter.backend.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactBookService {

    private final ContactBookRepository contactBookRepository;
    private final StudentRepository studentRepository;
    private final ContactBookMapper contactBookMapper;
    private final StudentMapper studentMapper;

    /**
     * Get all contact book entries for a specific student.
     * @param studentId ID of the student
     * @return List of {@link ResponseContactBookDTO}
     * @throws StudentNotFoundException if the student with the given ID does not exist
     */
    public List<ResponseContactBookDTO> getEntriesByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student mit der ID " + studentId + " nicht gefunden"));

        // Map each ContactBook entity to ResponseContactBookDTO including the associated student info and return the list
        return contactBookRepository.findContactBooksByStudent_Id(studentId)
                .stream()
                .map(contactBook -> {
                    ResponseStudentDTO studentDTO = studentMapper.toResponseDTO(student);
                    return contactBookMapper.toResponseDTO(contactBook, studentDTO);
                })
                .toList();
    }

    /**
     * Get a contact book entry by its ID.
     * @param contactBookId ID of the contact book entry
     * @return {@link ResponseContactBookDTO} with contact book details
     * @throws ResourceNotFoundException if the contact book entry with the given ID does not exist
     */
    public ResponseContactBookDTO getContactBookById(Long contactBookId) {
        ContactBook contactBook = contactBookRepository.findById(contactBookId)
                .orElseThrow(() -> new ResourceNotFoundException("Kontaktbucheintrag mit der ID " + contactBookId + " nicht gefunden"));

        ResponseStudentDTO studentDTO = studentMapper.toResponseDTO(contactBook.getStudent());
        return contactBookMapper.toResponseDTO(contactBook, studentDTO);
    }

    /**
     * Create a new contact book entry for a specific student.
     * @param requestContactBookDTO request DTO containing contact book details
     * @param studentId ID of the student to associate with the contact book entry
     * @return {@link ResponseContactBookDTO} of the created contact book entry
     * @throws StudentNotFoundException if the student with the given ID does not exist
     */
    public ResponseContactBookDTO create(RequestContactBookDTO requestContactBookDTO, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student mit der ID " + studentId + " nicht gefunden"));

        ContactBook contactBook = contactBookMapper.toEntity(requestContactBookDTO, student);
        contactBook = contactBookRepository.save(contactBook);

        ResponseStudentDTO studentDTO = studentMapper.toResponseDTO(student);
        return contactBookMapper.toResponseDTO(contactBook, studentDTO);
    }

    /**
     * Update an existing contact book entry.
     * @param contactBookId ID of the contact book entry to update
     * @param requestContactBookDTO request DTO containing updated fields
     * @return {@link ResponseContactBookDTO} of the updated contact book entry
     * @throws ResourceNotFoundException if the contact book entry with the given ID does not exist
     */
    public ResponseContactBookDTO update(Long contactBookId, RequestContactBookDTO requestContactBookDTO) {
        ContactBook contactBook = contactBookRepository.findById(contactBookId)
                .orElseThrow(() -> new ResourceNotFoundException("Kontaktbucheintrag mit der ID " + contactBookId + " nicht gefunden"));

        // Update fields if they are provided in the DTO (not null)
        contactBook.setCompanyName(requestContactBookDTO.getCompanyName());
        if (requestContactBookDTO.getContactPerson() != null) {
            contactBook.setContactPerson(requestContactBookDTO.getContactPerson());
        }
        if (requestContactBookDTO.getContactEmail() != null) {
            contactBook.setContactEmail(requestContactBookDTO.getContactEmail());
        }
        if (requestContactBookDTO.getContactPhone() != null) {
            contactBook.setContactPhone(requestContactBookDTO.getContactPhone());
        }
        contactBook.setApplicationChannel(requestContactBookDTO.getApplicationChannel());
        contactBook.setApplicationStatus(requestContactBookDTO.getApplicationStatus());
        contactBook.setAdPath(requestContactBookDTO.getAdPath());

        // Save the updated contact book entry
        contactBook = contactBookRepository.save(contactBook);

        // Map the associated student to ResponseStudentDTO for inclusion in the response DTO
        ResponseStudentDTO studentDTO = studentMapper.toResponseDTO(contactBook.getStudent());

        // Return the updated contact book entry as a Response DTO
        return contactBookMapper.toResponseDTO(contactBook, studentDTO);
    }

    /**
     * Delete a contact book entry by ID.
     * @param contactBookId ID of the contact book entry to delete
     * @throws ResourceNotFoundException if the contact book entry with the given ID does not exist
     */
    public void delete(Long contactBookId) {
        ContactBook contactBook = contactBookRepository.findById(contactBookId)
                .orElseThrow(() -> new ResourceNotFoundException("Kontaktbucheintrag mit der ID " + contactBookId + " nicht gefunden"));

        contactBookRepository.delete(contactBook);
    }
}

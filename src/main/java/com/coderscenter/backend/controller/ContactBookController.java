package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.contactBook.RequestContactBookDTO;
import com.coderscenter.backend.dtos.contactBook.ResponseContactBookDTO;
import com.coderscenter.backend.services.ContactBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/contact-book")
@RequiredArgsConstructor
public class ContactBookController {

    private final ContactBookService contactBookService;

    /**
     * Get all contact book entries for a specific student.
     * @param studentId ID of the student
     * @return List of {@link ResponseContactBookDTO}
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ResponseContactBookDTO>> getEntriesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(contactBookService.getEntriesByStudent(studentId));
    }

    /**
     * Get a contact book entry by its ID.
     * @param contactBookId ID of the contact book entry
     * @return {@link ResponseContactBookDTO} with contact book details
     */
    @GetMapping("/{contactBookId}")
    public ResponseEntity<ResponseContactBookDTO> getById(@PathVariable Long contactBookId) {
        return ResponseEntity.ok(contactBookService.getContactBookById(contactBookId));
    }

    /**
     * Create a new contact book entry for a specific student.
     * @param requestContactBookDTO DTO containing contact book details
     * @param studentId ID of the student to associate with the contact book entry
     * @return {@link ResponseContactBookDTO} of the created contact book entry
     */
    @PostMapping("/student/{studentId}")
    public ResponseEntity<ResponseContactBookDTO> createEntry(
            @Valid @RequestBody RequestContactBookDTO requestContactBookDTO,
            @PathVariable Long studentId){
        return new ResponseEntity<>(contactBookService.create(requestContactBookDTO, studentId), HttpStatus.CREATED);
    }

    /**
     * Update an existing contact book entry.
     * @param requestContactBookDTO DTO containing updated fields
     * @param contactBookId ID of the contact book entry to update
     * @return {@link ResponseContactBookDTO} of the updated contact book entry
     */
    @PutMapping("/{contactBookId}")
    public ResponseEntity<ResponseContactBookDTO> updateEntry(
            @Valid @RequestBody RequestContactBookDTO requestContactBookDTO,
            @PathVariable Long contactBookId){
        return ResponseEntity.ok(contactBookService.update(contactBookId, requestContactBookDTO));
    }

    /**
     * Delete a contact book entry by its ID.
     * @param contactBookId ID of the contact book entry to delete
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{contactBookId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long contactBookId) {
        contactBookService.delete(contactBookId);
        return ResponseEntity.noContent().build();
    }
}

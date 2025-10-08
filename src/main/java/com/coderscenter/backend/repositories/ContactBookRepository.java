package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.management.ContactBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactBookRepository extends JpaRepository<ContactBook, Long> {
    List<ContactBook> findContactBooksByStudent_Id(Long studentId);
}

package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.profile.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByUser_Id(Long userId);
}

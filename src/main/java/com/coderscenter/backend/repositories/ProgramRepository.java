package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.group_management.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
}

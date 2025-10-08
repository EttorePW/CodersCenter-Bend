package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.schedule_management.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
}

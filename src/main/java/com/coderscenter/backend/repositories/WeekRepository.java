package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.schedule_management.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekRepository extends JpaRepository<Week, Long> {

}

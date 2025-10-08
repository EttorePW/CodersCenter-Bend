package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.schedule_management.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository  extends JpaRepository<Schedule, Long> {
    
    /**
     * Find schedule with weeks eagerly loaded (days and slots loaded separately to avoid MultipleBagFetchException)
     */
    @Query("SELECT DISTINCT s FROM Schedule s " +
           "LEFT JOIN FETCH s.weeks " +
           "WHERE s.scheduleId = :scheduleId")
    Optional<Schedule> findByIdWithWeeks(@Param("scheduleId") Long scheduleId);
}

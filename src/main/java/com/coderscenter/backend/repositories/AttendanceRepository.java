package com.coderscenter.backend.repositories;

import com.coderscenter.backend.entities.management.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByStudent_Id(Long studentId);

    List<Attendance> findAllByStudent_Group_GroupId(Long studentGroupGroupId);
}

package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.attendance.ResponseAttendanceDTO;
import com.coderscenter.backend.entities.management.Attendance;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class AttendanceMapper {

    public ResponseAttendanceDTO toResponseDTO(Attendance attendance) {

        return ResponseAttendanceDTO.builder()
                .attendanceId(attendance.getAttendanceId())
                .isPresent(attendance.isPresent())
                .date(attendance.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .reasonForAbsence(attendance.getReasonForAbsence())
                .student(attendance.getStudent().getId())
                .build();
    }

}

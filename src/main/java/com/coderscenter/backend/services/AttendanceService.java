package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.attendance.RequestAttendanceDTO;
import com.coderscenter.backend.dtos.attendance.ResponseAttendanceDTO;
import com.coderscenter.backend.entities.management.Attendance;
import com.coderscenter.backend.entities.profile.Student;
import com.coderscenter.backend.mapper.AttendanceMapper;
import com.coderscenter.backend.repositories.AttendanceRepository;
import com.coderscenter.backend.repositories.StudentRepository;
import com.coderscenter.backend.services.helperService.DateParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final AttendanceMapper attendanceMapper;
    private final DateParseService dateParseService;

    public List<ResponseAttendanceDTO> getAllAttendances() {

        List<Attendance> attendances = attendanceRepository.findAll();
        List<ResponseAttendanceDTO> responseAttendances = new ArrayList<>();

        attendances.forEach(attendance -> {
            responseAttendances.add(attendanceMapper.toResponseDTO(attendance));
        });

        return responseAttendances;

    }

    public ResponseAttendanceDTO getAttendanceById(Long id) {

        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Eintrag in der Datenbank gefunden!"));

        return attendanceMapper.toResponseDTO(attendance);
    }

    public List<ResponseAttendanceDTO> getAttendanceByStudentId(Long id) {

        List<Attendance> attendances = attendanceRepository.findAllByStudent_Id(id);
        List<ResponseAttendanceDTO> responseAttendances = new ArrayList<>();

        attendances.forEach(attendance -> {
            responseAttendances.add(attendanceMapper.toResponseDTO(attendance));
        });

        return responseAttendances;
    }

    public List<ResponseAttendanceDTO> getAttendanceByGroupId(Long id) {

        List<Attendance> attendances = attendanceRepository.findAllByStudent_Group_GroupId(id);
        List<ResponseAttendanceDTO> responseAttendances = new ArrayList<>();

        attendances.forEach(attendance -> {
            responseAttendances.add(attendanceMapper.toResponseDTO(attendance));
        });

        return responseAttendances;
    }

    public ResponseAttendanceDTO createNew(RequestAttendanceDTO requestAttendanceDTO)  {

        Student student = studentRepository.findById(requestAttendanceDTO.getStudent()).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Student in der Datenbank gefunden!"));
        Attendance attendance = Attendance.builder()
                .isPresent(requestAttendanceDTO.isPresent())
                .date(dateParseService.stringToLocalDate(requestAttendanceDTO.getDate()))
                .reasonForAbsence(requestAttendanceDTO.getReasonForAbsence())
                .student(student)
                .build();

        attendanceRepository.save(attendance);

        return attendanceMapper.toResponseDTO(attendance);
    }

    public ResponseAttendanceDTO updateAttendance(Long id, RequestAttendanceDTO requestAttendanceDTO) {

        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Eintrag in der Datenbank gefunden!"));

        attendance.setPresent(requestAttendanceDTO.isPresent());
        attendance.setDate(dateParseService.stringToLocalDate(requestAttendanceDTO.getDate()));
        attendance.setReasonForAbsence(requestAttendanceDTO.getReasonForAbsence());

        attendanceRepository.save(attendance);

        return attendanceMapper.toResponseDTO(attendance);
    }

    public Object deleteAttendance(Long id) {

        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Eintrag in der Datenbank gefunden!"));

        attendanceRepository.delete(attendance);

        return  "Eintrag mit id " + id + " erfolgreich gelöscht!";
    }

}

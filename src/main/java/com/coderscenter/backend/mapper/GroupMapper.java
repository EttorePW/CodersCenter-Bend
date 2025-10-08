package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.employee.response.ResponseEmployeeDTO;
import com.coderscenter.backend.dtos.group.response.ResponseGroupDTO;
import com.coderscenter.backend.dtos.group.response.ResponseGroupWithListDTO;
import com.coderscenter.backend.dtos.student.response.ResponseStudentDTO;
import com.coderscenter.backend.dtos.subject.ResponseSubjectDTO;
import com.coderscenter.backend.entities.group_management.Group;
import com.coderscenter.backend.entities.pk.course_subject.Course_Subject_Employee_in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMapper {

    private final ProgramMapper programMapper;
    private final SubjectMapper subjectMapper;
    private final EmployeeMapper employeeMapper;
    private final StudentMapper studentMapper;


    public ResponseGroupDTO toResponseDTO(Group group) {

        return ResponseGroupDTO.builder()
                .groupId(group.getGroupId())
                .name(group.getName())
                .startDate(group.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .endDate(group.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .isActive(group.isActive())
                .program(programMapper.toResponseDTO(group.getProgram()))
                .build();
    }
    public ResponseGroupWithListDTO toResponseWithListDTO(Group group) {

        List<ResponseStudentDTO> studentsDTO = new ArrayList<>();

        group.getStudents().forEach(student -> {
            studentsDTO.add(studentMapper.toResponseDTO(student));
        });

        List<ResponseEmployeeDTO> employees = group.getCourses().stream()
                .map(Course_Subject_Employee_in::getEmployee)
                .distinct()
                .map(employeeMapper::toResponseDTO)
                .toList();

        List<ResponseSubjectDTO> subjects = group.getCourses().stream()
                .map(Course_Subject_Employee_in::getSubject)
                .distinct()
                .map(subjectMapper::toResponseDTO)
                .toList();

        return ResponseGroupWithListDTO.builder()
                .groupId(group.getGroupId())
                .name(group.getName())
                .startDate(group.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .endDate(group.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .isActive(group.isActive())
                .program(programMapper.toResponseDTO(group.getProgram()))
                .employees(employees)
                .subjects(subjects)
                .students(studentsDTO)
                .build();
    }
}

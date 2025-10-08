package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.course.ResponseCourseDTO;
import com.coderscenter.backend.dtos.course.ResponseSubjectWithEmployeeDTO;
import com.coderscenter.backend.dtos.employee.response.ResponseEmployeeDTO;
import com.coderscenter.backend.dtos.group.request.AssingDTO;
import com.coderscenter.backend.dtos.group.request.RequestGroupDTO;
import com.coderscenter.backend.dtos.group.response.ResponseGroupDTO;
import com.coderscenter.backend.dtos.group.response.ResponseGroupWithListDTO;
import com.coderscenter.backend.dtos.subject.ResponseSubjectDTO;
import com.coderscenter.backend.entities.group_management.Group;
import com.coderscenter.backend.entities.group_management.Program;
import com.coderscenter.backend.entities.group_management.Subject;
import com.coderscenter.backend.entities.pk.course_subject.Course_Subject_Employee_in;
import com.coderscenter.backend.entities.profile.Employee;
import com.coderscenter.backend.entities.profile.Student;
import com.coderscenter.backend.exceptions.EmptyOptionalException;
import com.coderscenter.backend.exceptions.SubjectNotMatchException;
import com.coderscenter.backend.exceptions.ThereAreStudentsAssigned;
import com.coderscenter.backend.mapper.EmployeeMapper;
import com.coderscenter.backend.mapper.GroupMapper;
import com.coderscenter.backend.mapper.SubjectMapper;
import com.coderscenter.backend.repositories.*;
import com.coderscenter.backend.services.helperService.DateParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProgramRepository programRepository;
    private final GroupMapper groupMapper;
    private final SubjectRepository subjectRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;
    private final DateParseService dateParseService;
    private final SubjectMapper subjectMapper;
    private final EmployeeMapper employeeMapper;


    public List<ResponseGroupWithListDTO> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        List<ResponseGroupWithListDTO> ResponseGroupDTOs = new ArrayList<>();
        groups.forEach(group -> {
            ResponseGroupDTOs.add(groupMapper.toResponseWithListDTO(group));
        });

        return ResponseGroupDTOs;
    }

    public ResponseGroupWithListDTO getGroupById(long id) throws EmptyOptionalException {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            return groupMapper.toResponseWithListDTO(group.get());
        }
        throw new EmptyOptionalException("Unexpected empty Optional","/api/admin/group/" + id);
    }

    public ResponseCourseDTO getCoursesById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Keine entsprechende Gruppe in der Datenbank gefunden!"));

        // Map von SubjectId zu SubjectWithEmployeesDTO
        Map<Long, ResponseSubjectWithEmployeeDTO> subjectMap = new HashMap<>();

        for (Course_Subject_Employee_in course : group.getCourses()) {
            Long subjectId = course.getSubject().getSubjectId();
            ResponseEmployeeDTO employeeDTO = employeeMapper.toResponseDTO(course.getEmployee());

            subjectMap.compute(subjectId, (k, v) -> {
                if (v == null) {
                    v = ResponseSubjectWithEmployeeDTO.builder()
                            .subjectId(subjectId)
                            .name(course.getSubject().getName())
                            .employees(new ArrayList<>())
                            .build();
                }
                v.getEmployees().add(employeeDTO);
                return v;
            });
        }

        return ResponseCourseDTO.builder()
                .groupId(group.getGroupId())
                .subjects(new ArrayList<>(subjectMap.values()))
                .build();
    }

    public ResponseGroupDTO createNew(RequestGroupDTO requestGroupDTO) throws EmptyOptionalException {

        Program program = programRepository.findById(requestGroupDTO.getProgramId()).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechende Program in der Datenbank gefunden!"));

        Group group = Group.builder()
                .name(requestGroupDTO.getName())
                .startDate(dateParseService.stringToLocalDate(requestGroupDTO.getStartDate()))
                .endDate(dateParseService.stringToLocalDate(requestGroupDTO.getEndDate()))
                .isActive(requestGroupDTO.isActive())
                .program(program)
                .build();

        groupRepository.save(group);

        return groupMapper.toResponseDTO(group);
    }

    public ResponseGroupDTO updateGroup  ( Long id, RequestGroupDTO requestGroupDTO) throws EmptyOptionalException {
        Program program = programRepository.findById(requestGroupDTO.getProgramId()).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechende Program in der Datenbank gefunden!"));

        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            group.get().setName(requestGroupDTO.getName());
            group.get().setStartDate(dateParseService.stringToLocalDate(requestGroupDTO.getStartDate()));
            group.get().setEndDate(dateParseService.stringToLocalDate(requestGroupDTO.getEndDate()));
            group.get().setActive(requestGroupDTO.isActive());
            group.get().setProgram(program);


            groupRepository.save(group.get());
            return groupMapper.toResponseDTO(group.get());
        }
        throw new EmptyOptionalException("Unexpected empty Optional","/api/admin/group/" + id);
    }

    public ResponseGroupWithListDTO assignToGroup(Long id, AssingDTO assingDTO) throws EmptyOptionalException {

        //Trainer und Facher zugewiesen
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            for(Long subjectId : assingDTO.getSubjectEmployee().keySet()){
                Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechendes Fach in der Datenbank gefunden!"));

                List<Long> employeeIds = assingDTO.getSubjectEmployee().get(subjectId);
                for (Long employeeId : employeeIds){
                    Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Trainer in der Datenbank gefunden!"));

                    if (!employee.getSubjects().contains(subject)) {
                        throw new SubjectNotMatchException("The Trainer does not teach the Subject","/api/admin/group/" + id);
                    }

                    group.get().addAssignment(employee,subject);
                }

            }
            //Teilnehmer zuweisen
            assingDTO.getStudents().forEach(studentId -> {
                Student student = studentRepository.findById(studentId).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Teilnehmer in der Datenbank gefunden!"));
                student.setGroup(group.get());
                group.get().getStudents().add(student);

            });


            groupRepository.save(group.get());
            return groupMapper.toResponseWithListDTO(group.get());
        }
        throw new EmptyOptionalException("Unexpected empty Optional","/api/admin/group/" + id);
    }
    //add method to remove assignment
    public ResponseGroupWithListDTO removeAssignment(Long groupId, Long employeeId) throws EmptyOptionalException {
        //Trainer und Facher zugewiesen
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new UsernameNotFoundException("Kein entsprechender Trainer in der Datenbank gefunden!"));

        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            group.get().removeAssignment(employee);
            groupRepository.save(group.get());
        }
        return groupMapper.toResponseWithListDTO(group.get());
    }

    public Object deleteGroup(long id) throws EmptyOptionalException {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            if (group.get().getStudents().isEmpty()) {
                groupRepository.delete(group.get());
                return "Delete Group with id " + id + " successfully!";
            }else{
                throw new ThereAreStudentsAssigned("Es sind immer noch Studenten zugewiesen in der Gruppe","/api/admin/group/" + id);
            }

        }
        throw new EmptyOptionalException("Unexpected empty Optional, could not been deleted","/api/admin/group/" + id);
    }
}

package com.coderscenter.backend.dtos.group.response;

import com.coderscenter.backend.dtos.employee.response.ResponseEmployeeDTO;
import com.coderscenter.backend.dtos.program.ResponseProgramDTO;
import com.coderscenter.backend.dtos.student.response.ResponseStudentDTO;
import com.coderscenter.backend.dtos.subject.ResponseSubjectDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class ResponseGroupWithListDTO {

    private Long groupId;
    private String name;
    private String startDate;
    private String endDate;
    private boolean isActive;
    private ResponseProgramDTO program;
    private List<ResponseEmployeeDTO> employees;
    private List<ResponseSubjectDTO> subjects;
    private List<ResponseStudentDTO> students;

}

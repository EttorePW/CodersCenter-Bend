package com.coderscenter.backend.dtos.course;

import com.coderscenter.backend.dtos.employee.response.ResponseEmployeeDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseSubjectWithEmployeeDTO {

    private Long subjectId;
    private String name;
    private List<ResponseEmployeeDTO> employees;

}
